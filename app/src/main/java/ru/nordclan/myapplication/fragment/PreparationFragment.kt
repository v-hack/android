package ru.nordclan.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.adapter.PreparationsAdapter
import ru.nordclan.myapplication.adapter.SimpleItemTouchHelperCallback
import ru.nordclan.myapplication.api.ApiError
import ru.nordclan.myapplication.api.DrugResponse
import ru.nordclan.myapplication.api.NetworkError

class PreparationFragment(private val userId: Long = 1) : BaseFragment() {

    private var disposable: Disposable? = null
    private val preparations = mutableListOf<DrugResponse>()
    private lateinit var preparationView: RecyclerView
    private var progressDialog: DialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_preparation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preparationView = view.findViewById<RecyclerView>(R.id.rv_preparation).also { rv ->
            rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rv.setHasFixedSize(true)
            rv.addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).also { divider ->
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider)
                        ?.let { drawable ->
                            divider.setDrawable(drawable)
                        }
                })
        }

        disposable = api.preparations(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                preparations.clear()
                progressDialog = ProgressFragment().also {
                    it.show(childFragmentManager, null)
                }
            }
            .doFinally {
                progressDialog?.dismiss()
                val adapter = PreparationsAdapter(preparations)
                preparationView.adapter = adapter

                val callback = SimpleItemTouchHelperCallback(adapter)
                val itemTouchHelper = ItemTouchHelper(callback)
                itemTouchHelper.attachToRecyclerView(preparationView)

            }
            .subscribe { resp ->
                when (resp) {
                    is ApiError ->
                        println()
                    is NetworkError ->
                        println()
                    is DrugResponse ->
                        preparations.add(resp)
                }
            }
    }

    override fun onDetach() {
        disposable?.dispose()
        super.onDetach()
    }
}

package ru.nordclan.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDateTime
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.adapter.VisitsAdapter
import ru.nordclan.myapplication.api.ApiError
import ru.nordclan.myapplication.api.NetworkError
import ru.nordclan.myapplication.api.VisitResponse

class VisitsFragment(private val userId: Long = 1) : BaseFragment() {

    private var disposable: Disposable? = null
    private val visits = mutableListOf<VisitResponse>()
    private lateinit var planView: RecyclerView
    private lateinit var pastView: RecyclerView
    private var progressDialog: DialogFragment? = null

    private val pastAdapter by lazy {
        VisitsAdapter(
            arrayOf(
                VisitResponse(
                    LocalDateTime.now().minusDays(1),
                    1,
                    1,
                    "Копипастова Копипаста Копипастовна",
                    "Уролог-гинеколог-практолог-тд"
                ),
                VisitResponse(
                    LocalDateTime.now().minusDays(2),
                    1,
                    1,
                    "Копипастова Копипаста Копипастовна",
                    "Уролог-гинеколог-практолог-тд"
                ),
                VisitResponse(
                    LocalDateTime.now().minusDays(3),
                    1,
                    1,
                    "Копипастова Копипаста Копипастовна",
                    "Уролог-гинеколог-практолог-тд"
                ),
                VisitResponse(
                    LocalDateTime.now().minusDays(4),
                    1,
                    1,
                    "Копипастова Копипаста Копипастовна",
                    "Уролог-гинеколог-практолог-тд"
                ),
                VisitResponse(
                    LocalDateTime.now().minusDays(5),
                    1,
                    1,
                    "Копипастова Копипаста Копипастовна",
                    "Уролог-гинеколог-практолог-тд"
                )
            )
        )
    }

    override fun onDetach() {
        disposable?.dispose()
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_visits, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        planView = view.findViewById<RecyclerView>(R.id.rv_plan).also { rv ->
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

        pastView = view.findViewById<RecyclerView>(R.id.rv_past).also { rv ->
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

        disposable = api.visits(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                visits.clear()
                progressDialog = ProgressFragment().also {
                    it.show(childFragmentManager, null)
                }
            }
            .doFinally {
                progressDialog?.dismiss()
                planView.adapter = VisitsAdapter(visits.toTypedArray())
                pastView.adapter = pastAdapter
            }
            .subscribe { resp ->
                when (resp) {
                    is ApiError ->
                        println()
                    is NetworkError ->
                        println()
                    is VisitResponse ->
                        visits.add(resp)
                }
            }
    }
}

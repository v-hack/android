package ru.nordclan.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.view.FloatSeekBar
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment() {

    private lateinit var seekBarView: FloatSeekBar
    private lateinit var tempView: TextView
    private var disposable: Disposable? = null
    private var progressDialog: DialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tempView = view.findViewById(R.id.txt_temp)

        seekBarView = view.findViewById<FloatSeekBar>(R.id.seek_bar).also { sb ->
            sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tempView.text = "%.1f".format(sb.value)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
            sb.value = 36.65F
        }

        view.findViewById<TextView>(R.id.txt_approve).let {
            it.setOnClickListener {
                Single.timer(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        progressDialog = ProgressFragment().also { pf ->
                            pf.show(childFragmentManager, null)
                        }
                    }
                    .doFinally {
                        progressDialog?.dismiss()
                    }
                    .subscribe()
            }
        }
    }

    override fun onDetach() {
        disposable?.dispose()
        super.onDetach()
    }
}

package ru.nordclan.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.format.DateTimeFormat
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.api.ApiError
import ru.nordclan.myapplication.api.NetworkError
import ru.nordclan.myapplication.api.PatientInfoResponse

class ProfileFragment(private val userId: Long = 1) : BaseFragment() {

    private var progressDialog: DialogFragment? = null
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        disposable = api.patientInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                progressDialog = ProgressFragment().also {
                    it.show(childFragmentManager, null)
                }
            }
            .doFinally {
                progressDialog?.dismiss()
            }
            .subscribe { resp ->
                when (resp) {
                    is ApiError ->
                        println()
                    is NetworkError ->
                        println()
                    is PatientInfoResponse ->
                        fillPatient(view, resp)
                }
            }
    }

    private fun fillPatient(view: View, info: PatientInfoResponse) {
        Glide.with(requireContext())
            .load(R.mipmap.ic_patient)
            .circleCrop()
            .into(view.findViewById(R.id.img_avatar))

        view.findViewById<TextView>(R.id.txt_name).text =
            "${info.lastName} ${info.firstName.first().toUpperCase()}."
        view.findViewById<TextView>(R.id.edt_gender).text =
            if (info.gender == 1) "Мужчина" else "Женщина"
        view.findViewById<TextView>(R.id.edt_age).text = info.age.toString()
        view.findViewById<TextView>(R.id.edt_city).text = info.city
        view.findViewById<TextView>(R.id.edt_date).text =
            info.receiptDate.toString(DateTimeFormat.shortDate())
        view.findViewById<TextView>(R.id.edt_diagn).text = info.diagnosis
        view.findViewById<TextView>(R.id.edt_lab).text = info.laboratoryData

    }

    override fun onDetach() {
        disposable?.dispose()
        super.onDetach()
    }
}

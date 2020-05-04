package ru.nordclan.myapplication.fragment

import android.content.Context
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import ru.nordclan.myapplication.MyApplication
import ru.nordclan.myapplication.api.Api
import javax.inject.Inject

open class BaseFragment : Fragment() {

    @Inject
    lateinit var api: Api

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as MyApplication).appComponent.inject(this)
    }
}

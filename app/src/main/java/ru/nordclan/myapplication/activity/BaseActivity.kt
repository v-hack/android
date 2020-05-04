package ru.nordclan.myapplication.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.Disposable
import ru.nordclan.myapplication.MyApplication
import ru.nordclan.myapplication.api.Api
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    private val disposables = mutableListOf<Disposable>()

    @Inject
    lateinit var api: Api

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as MyApplication).appComponent.inject(this)
    }

    fun register(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onStop() {
        disposables.forEach {
            it.dispose()
        }
        disposables.clear()
        super.onStop()
    }
}

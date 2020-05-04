package ru.nordclan.myapplication.activity

import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import ru.nordclan.myapplication.R

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        register(
            getFCMToken()
                .flatMap { token ->
                    api.sendToken(token)
                }
                .subscribe { _ ->
                    if (registered()) {
                        MainActivity.start(this)
                        finish()
                    } else {
                        LoginActivity.start(this)
                    }
                })
    }

    // todo
    private fun registered(): Boolean =
        false

    private fun getFCMToken(): Single<String> =
        Single.fromObservable { observer ->
            FirebaseInstanceId.getInstance()
                .instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result?.token
                        if (token != null) {
                            observer.onNext(token)
                        }
                    }
                    observer.onComplete()
                }
        }
}

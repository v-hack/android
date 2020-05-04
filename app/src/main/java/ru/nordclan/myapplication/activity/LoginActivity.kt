package ru.nordclan.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.fragment.ProgressFragment
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity() {

    companion object {

        private const val DELAY_IN_SEC = 1L

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    private lateinit var mLoginView: TextView
    private lateinit var mPasswordView: TextView
    private var progressDialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        mLoginView = findViewById(R.id.txt_login)
        mPasswordView = findViewById(R.id.txt_password)
        findViewById<Button>(R.id.btn_login).setOnClickListener {

            // todo
            val login = mLoginView.text.toString()
            val password = mPasswordView.text.toString()

            register(Single.timer(DELAY_IN_SEC, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progressDialog = ProgressFragment().also {
                        it.show(supportFragmentManager, null)
                    }
                }
                .doFinally {
                    progressDialog?.dismiss()
                }
                .subscribe { _ ->
                    MainActivity.start(this)
                    finish()
                })
        }

//        findViewById<View>(R.id.txt_register).setOnClickListener {
//            // todo
//            Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show()
//        }
    }
}

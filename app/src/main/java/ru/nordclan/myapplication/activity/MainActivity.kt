package ru.nordclan.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.fragment.*

class MainActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    private val mainFragment by lazy { MainFragment() }
    private val chatFragment by lazy { ChatFragment() }
    private val visitsFragment by lazy { VisitsFragment() }
    private val profileFragment by lazy { ProfileFragment() }
    private val preparationFragment by lazy { PreparationFragment() }

    private lateinit var mBottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation).also {
            it.setOnNavigationItemSelectedListener { item ->
                if (item.itemId == it.selectedItemId) {
                    true
                } else {
                    when (item.itemId) {
                        R.id.preparation -> {
                            supportFragmentManager.commit {
                                replace(R.id.frm_container, preparationFragment, null)
                            }
                            true
                        }
                        R.id.visits -> {
                            supportFragmentManager.commit {
                                replace(R.id.frm_container, visitsFragment, null)
                            }
                            true
                        }
                        R.id.main -> {
                            supportFragmentManager.commit {
                                replace(R.id.frm_container, mainFragment, null)
                            }
                            true
                        }
                        R.id.chat -> {
                            supportFragmentManager.commit {
                                replace(R.id.frm_container, chatFragment, null)
                            }
                            true
                        }
                        R.id.profile -> {
                            supportFragmentManager.commit {
                                replace(R.id.frm_container, profileFragment, null)
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
            it.selectedItemId = R.id.main
        }
    }
}

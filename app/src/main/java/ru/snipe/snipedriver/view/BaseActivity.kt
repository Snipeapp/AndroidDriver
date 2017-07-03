package ru.snipe.snipedriver.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.createFragment

abstract class BaseActivity<out T : Fragment> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, createFragment(
                            getFragment(),
                            { putAll(intent?.extras ?: Bundle()) }
                    ))
                    .commit()
        }
    }

    abstract fun getFragment(): T
}
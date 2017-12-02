package ru.snipe.snipedriver.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.createFragment

abstract class BaseActivity<out T : Fragment> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_base)
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
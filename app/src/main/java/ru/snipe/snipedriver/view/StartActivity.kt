package ru.snipe.snipedriver.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.view.onboarding.OnBoardingFragment

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, OnBoardingFragment())
                .commit()
    }
}


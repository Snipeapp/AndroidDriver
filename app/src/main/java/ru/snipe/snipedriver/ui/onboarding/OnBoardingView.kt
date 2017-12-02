package ru.snipe.snipedriver.ui.onboarding

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface OnBoardingView : MvpView {
    fun loginButtonClicked(): Observable<Any>
    fun signUpButtonClicked(): Observable<Any>
    fun success()
    fun showError(error: String)
}
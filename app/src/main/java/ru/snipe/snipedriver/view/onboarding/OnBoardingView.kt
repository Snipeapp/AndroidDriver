package ru.snipe.snipedriver.view.onboarding

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface OnBoardingView : MvpView {
    fun loginButtonClicked(): Observable<Object>
    fun signUpButtonClicked(): Observable<Object>
    fun success()
    fun showError(error: String)
}
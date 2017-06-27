package ru.snipe.snipedriver.view.onboarding

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface OnBoardingView : MvpView {
    fun loginButtonClicked(): Observable<Boolean>
    fun signUpButtonClicked(): Observable<Boolean>
}
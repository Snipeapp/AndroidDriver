package ru.snipe.snipedriver.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import ru.snipe.snipedriver.view.onboarding.OnBoardingView
import javax.inject.Inject

class OnBoardingPresenter
@Inject constructor() : MviBasePresenter<OnBoardingView, OnBoardingViewState>() {
    override fun bindIntents() {
    }
}

class OnBoardingViewState
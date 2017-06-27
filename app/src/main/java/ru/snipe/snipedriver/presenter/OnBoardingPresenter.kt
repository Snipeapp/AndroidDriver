package ru.snipe.snipedriver.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.snipe.snipedriver.data.DataManager
import ru.snipe.snipedriver.view.onboarding.OnBoardingView
import javax.inject.Inject

class OnBoardingPresenter
@Inject constructor(var dataManager: DataManager) : MviBasePresenter<OnBoardingView, OnBoardingViewState>() {
    override fun bindIntents() {
        val sub: Observable<LoginViewState>
                = intent { it.loginButtonClicked() }
                .map { dataManager.login(); LoginViewState.LoggedInViewState() }

//        subscribeViewState(sub, OnBoardingView::render)
    }
}

class OnBoardingViewState
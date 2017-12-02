package ru.snipe.snipedriver.ui.onboarding

import com.arellomobile.mvp.InjectViewState
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.snipe.snipedriver.network.DataManager
import ru.snipe.snipedriver.ui.base_mvp.MoxyRxPresenter

@InjectViewState
class OnBoardingPresenter(var dataManager: DataManager,
                          val networkObservable: Observable<Connectivity>) : MoxyRxPresenter<OnBoardingView>() {
  private val networkRelay = BehaviorRelay.create<Connectivity>()

  override fun onFirstViewAttach() {
    networkObservable.subscribe(networkRelay)
  }

  fun onSignUpButtonClicked() = loginOrError()

  fun onLoginButtonClicked() = loginOrError()

  private fun loginOrError() {
    networkRelay
      .map { it.isAvailable }
      .subscribeP(
        { hasInternet ->
          if (hasInternet)
            viewState.switchToPhoneInsertScreen()
          else
            viewState.showErrorMessage("Нет соединения с интернетом")
        })
  }
}
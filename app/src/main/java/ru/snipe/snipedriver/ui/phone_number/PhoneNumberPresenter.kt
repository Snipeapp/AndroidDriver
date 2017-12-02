package ru.snipe.snipedriver.ui.phone_number

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import ru.snipe.snipedriver.network.DataManager
import ru.snipe.snipedriver.ui.base_mvp.MoxyRxPresenter

@InjectViewState
class PhoneNumberPresenter(val dataManager: DataManager)
  : MoxyRxPresenter<PhoneNumberView>() {

  fun onPhoneValid(phone: String) {
    viewState.showLoading()
    dataManager.sendCode(phone)
      .toObservable<Boolean>()
      .concatWith(Observable.just(true))
      .doOnNext { viewState.hideLoading() }
      .subscribeP({ viewState.codeSent() })
  }
}
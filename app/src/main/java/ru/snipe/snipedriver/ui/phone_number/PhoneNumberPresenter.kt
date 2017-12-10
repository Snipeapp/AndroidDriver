package ru.snipe.snipedriver.ui.phone_number

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.network.DataManager
import ru.snipe.snipedriver.ui.base_mvp.MoxyRxPresenter

@InjectViewState
class PhoneNumberPresenter(private val dataManager: DataManager) : MoxyRxPresenter<PhoneNumberView>() {

  fun onNextClicked(text: String) {
    val isPhone = text.replace("[^0-9]+".toRegex(), "").matches("[0-9]{11}|9[0-9]{9}".toRegex())
    if (isPhone) {
      onPhoneValid(text)
    } else {
      viewState.showError(R.string.phone_number_error_wrong_number)
    }
  }

  private fun onPhoneValid(phone: String) {
    viewState.showLoading()
    dataManager.sendCode(phone)
      .toObservable<Boolean>()
      .concatWith(Observable.just(true))
      .doOnNext { viewState.hideLoading() }
      .subscribeP({ viewState.codeSent() })
  }
}
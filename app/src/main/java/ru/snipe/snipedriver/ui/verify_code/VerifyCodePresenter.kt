package ru.snipe.snipedriver.ui.verify_code

import com.arellomobile.mvp.InjectViewState
import ru.snipe.snipedriver.network.DataManager
import ru.snipe.snipedriver.ui.base_mvp.MoxyRxPresenter

@InjectViewState
class VerifyCodePresenter(private val dataManager: DataManager,
                          private val phone: String) : MoxyRxPresenter<VerifyCodeView>() {

  fun onCodeValidated(code: String) {
    viewState.showLoading()
    dataManager.verifyCode(phone, code)
      .doOnNext { viewState.hideLoading() }
      .subscribeP({ verified ->
        if (verified)
          viewState.codeVerified()
        else
          viewState.showError("Неправильный код")
      })
  }

  fun onResendButtonClicked() {
    viewState.showLoading()
    dataManager.resendCode(phone)
      .subscribeP(
        {
          viewState.hideLoading()
          viewState.codeSent()
        })
  }
}
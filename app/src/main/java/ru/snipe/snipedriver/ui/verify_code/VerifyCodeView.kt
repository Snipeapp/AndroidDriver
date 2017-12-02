package ru.snipe.snipedriver.ui.verify_code

import ru.snipe.snipedriver.ui.base_mvp.ElceView

interface VerifyCodeView : ElceView<Unit> {
  fun showLoading()
  fun hideLoading()
  fun codeSent()
  fun codeVerified()
  fun showError(error: String)
}
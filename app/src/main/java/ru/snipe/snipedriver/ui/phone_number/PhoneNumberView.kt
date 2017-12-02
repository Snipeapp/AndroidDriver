package ru.snipe.snipedriver.ui.phone_number

import ru.snipe.snipedriver.ui.base_mvp.ElceView

interface PhoneNumberView : ElceView<Unit> {
  fun showLoading()
  fun hideLoading()
  fun codeSent()
  fun showError(error: String)
}
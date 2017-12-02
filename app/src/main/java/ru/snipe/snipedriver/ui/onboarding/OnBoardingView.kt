package ru.snipe.snipedriver.ui.onboarding

import ru.snipe.snipedriver.ui.base_mvp.ElceView

interface OnBoardingView : ElceView<Unit> {
  fun switchToPhoneInsertScreen()
  fun showErrorMessage(error: String)
}
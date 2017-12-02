package ru.snipe.snipedriver.ui.free_driver_mode

import ru.snipe.snipedriver.ui.base_mvp.ElceView

interface FreeDriverMainView : ElceView<Unit> {
  fun showLoading()
  fun hideLoading()
  fun driveRequest()
  fun showError(error: String)
  fun setStatus(activated: Boolean)
  fun goToDriverMode()
}
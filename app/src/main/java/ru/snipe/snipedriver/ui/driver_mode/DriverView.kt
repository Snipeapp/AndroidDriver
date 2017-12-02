package ru.snipe.snipedriver.ui.driver_mode

import ru.snipe.snipedriver.ui.base_mvp.ElceView

interface DriverView : ElceView<Unit> {
    fun showLoading()
    fun hideLoading()
    fun showError(error: String)
    fun goToBeginDeliveryMode()
    fun askForArrive()
    fun goToDeliveryMode()
    fun askForDeliveryArrive()
    fun goToRatingScreen()
    fun deliveryFinished()
}
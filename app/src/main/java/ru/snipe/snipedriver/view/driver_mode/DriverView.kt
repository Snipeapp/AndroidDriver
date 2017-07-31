package ru.snipe.snipedriver.view.driver_mode

import com.hannesdorfmann.mosby3.mvp.MvpView

interface DriverView : MvpView {
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
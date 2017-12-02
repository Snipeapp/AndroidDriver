package ru.snipe.snipedriver.ui.free_driver_mode

import com.hannesdorfmann.mosby3.mvp.MvpView

interface FreeDriverView : MvpView {
    fun showLoading()
    fun hideLoading()
    fun driveRequest()
    fun showError(error: String)
    fun setStatus(activated: Boolean)
    fun goToDriverMode()
}
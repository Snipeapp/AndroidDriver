package ru.snipe.snipedriver.ui.phone_number

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface PhoneNumberView : MvpView {
    fun showLoading()
    fun hideLoading()
    fun nextClicked(): Observable<String>
    fun codeSent()
    fun showError(error: String)
}
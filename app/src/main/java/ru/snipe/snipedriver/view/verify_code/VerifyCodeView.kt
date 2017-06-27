package ru.snipe.snipedriver.view.verify_code

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.Subject

interface VerifyCodeView : MvpView {
    fun showLoading()
    fun hideLoading()
    fun resendClicked(): Subject<Boolean>
    fun readyClicked(): Observable<String>
    fun codeVerified()
    fun showError(error: String)
}
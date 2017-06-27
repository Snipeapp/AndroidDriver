package ru.snipe.snipedriver.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.snipe.snipedriver.presenter.LoginViewState

interface LoginView : MvpView {
    fun loginButtonClicked(): Observable<Boolean>
    fun render(state: LoginViewState)
}
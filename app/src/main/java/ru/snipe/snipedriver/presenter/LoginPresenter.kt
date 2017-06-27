package ru.snipe.snipedriver.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.snipe.snipedriver.data.DataManager
import ru.snipe.snipedriver.view.LoginView
import javax.inject.Inject

class LoginPresenter
@Inject constructor(var dataManager: DataManager) : MviBasePresenter<LoginView, LoginViewState>() {
    override fun bindIntents() {
        val sub: Observable<LoginViewState>
                = intent { it.loginButtonClicked() }
                .map { dataManager.login(); LoginViewState.LoggedInViewState() }

        subscribeViewState(sub, LoginView::render)
    }
}

sealed class LoginViewState {
    class LoggedInViewState() : LoginViewState()
}

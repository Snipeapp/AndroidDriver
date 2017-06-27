package ru.snipe.snipedriver.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import ru.snipe.snipedriver.view.verify_code.VerifyCodeView
import javax.inject.Inject

class VerifyCodePresenter
@Inject constructor() : MviBasePresenter<VerifyCodeView, VerifyCodeViewState>() {
    override fun bindIntents() {
    }
}

class VerifyCodeViewState
package ru.snipe.snipedriver.presenter

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import ru.snipe.snipedriver.view.phone_number.PhoneNumberView
import javax.inject.Inject

class PhoneNumberPresenter
@Inject constructor() : MviBasePresenter<PhoneNumberView, PhoneNumberViewState>() {
    override fun bindIntents() {
    }
}

class PhoneNumberViewState
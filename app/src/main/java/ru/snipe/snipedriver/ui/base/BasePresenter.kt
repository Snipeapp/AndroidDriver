package ru.snipe.snipedriver.ui.base

import com.hannesdorfmann.mosby3.mvp.MvpView

open class BasePresenter<V : MvpView> {
    var view: V? = null

    open fun attachView(v: V) {
        this.view = v
    }

    open fun detachView() {
        view = null
    }
}
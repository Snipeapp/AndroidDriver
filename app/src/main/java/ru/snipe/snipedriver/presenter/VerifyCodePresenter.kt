package ru.snipe.snipedriver.presenter

import io.reactivex.disposables.CompositeDisposable
import ru.snipe.snipedriver.data.DataManager
import ru.snipe.snipedriver.view.verify_code.VerifyCodeView
import javax.inject.Inject

class VerifyCodePresenter
@Inject constructor(val dataManager: DataManager) : BasePresenter<VerifyCodeView>() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var phone: String

    override fun attachView(v: VerifyCodeView) {
        super.attachView(v)
        view?.apply {
            compositeDisposable.apply {
                add(resendClicked()
                        .doOnNext { view?.showLoading() }
                        .concatMap { dataManager.resendCode(phone) }
                        .doOnNext { view?.hideLoading(); view?.codeSent() }
                        .subscribe())

                add(readyClicked()
                        .doOnNext { view?.showLoading() }
                        .concatMap { dataManager.verifyCode(phone, it) }
                        .doOnNext { view?.hideLoading() }
                        .subscribe({ verified ->
                            if (verified) view?.codeVerified() else view?.showError("Неправильный код")
                        }))
                return
            }
            return
        }
    }

    override fun detachView() {
        compositeDisposable.dispose()
        super.detachView()
    }
}

//class VerifyCodePresenter
//@Inject constructor() : MviBasePresenter<VerifyCodeView, VerifyCodeViewState>() {
//    override fun bindIntents() {
//        intent { it.resendClicked() }
//
//    }
//}
//
//class VerifyCodeViewState
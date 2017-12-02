package ru.snipe.snipedriver.ui.phone_number

import com.github.pwittchen.reactivenetwork.library.Connectivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import ru.snipe.snipedriver.network.DataManager
import javax.inject.Inject

class PhoneNumberPresenter
@Inject constructor(
        val dataManager: DataManager,
        val connectivityObservable: Observable<Connectivity>
) : BasePresenter<PhoneNumberView>() {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(v: PhoneNumberView) {
        super.attachView(v)
        view?.apply {
            compositeDisposable.apply {
                add(nextClicked()
                        .doOnNext { view?.showLoading() }
                        .concatMap { dataManager.sendCode(it).toObservable<Boolean>().concatWith(Observable.just(true)) }
                        .doOnNext { view?.hideLoading() }
                        .subscribe({ view?.codeSent() }))
                return
            }
            return
        }
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }
}
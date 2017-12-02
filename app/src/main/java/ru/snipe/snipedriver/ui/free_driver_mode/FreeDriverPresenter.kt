package ru.snipe.snipedriver.ui.free_driver_mode

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.snipe.snipedriver.network.DataManager
import ru.snipe.snipedriver.ui.base.BasePresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FreeDriverPresenter @Inject constructor(val dataManager: DataManager) : BasePresenter<FreeDriverView>() {
    private var currentStatus: Boolean = false
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(v: FreeDriverView) {
        super.attachView(v)
        dataManager.getStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it) Observable.timer(2, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { view?.driveRequest() }
                }
                .subscribe({
                    view?.setStatus(it)
                    currentStatus = it
                })
    }

    fun statusClicked() {
        Observable.just(!currentStatus)
                .doOnNext { view?.showLoading() }
                .concatMap { dataManager.setStatus(it).toObservable<Any>() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe()
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    fun requestAccepted() {
        view?.goToDriverMode()
    }
}
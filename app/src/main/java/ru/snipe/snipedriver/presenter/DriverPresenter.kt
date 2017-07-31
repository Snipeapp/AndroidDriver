package ru.snipe.snipedriver.presenter

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.snipe.snipedriver.data.DataManager
import ru.snipe.snipedriver.view.driver_mode.DriverView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DriverPresenter @Inject constructor(val dataManager: DataManager) : BasePresenter<DriverView>() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val STATE_RIDING: Int = 0
    val STATE_BEGIN_DELIVERY: Int = 1
    val STATE_DELIVERY: Int = 2
    var state = STATE_RIDING

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    fun driverArrived() {
        state = STATE_BEGIN_DELIVERY
        Observable.timer(1, TimeUnit.SECONDS)
                .doOnSubscribe { view?.showLoading() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe { view?.goToBeginDeliveryMode() }
    }

    fun driverDeliveryArrived() {
        Observable.timer(1, TimeUnit.SECONDS)
                .doOnSubscribe { view?.showLoading() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe { view?.goToRatingScreen() }
    }

    fun moveToNextState() {
        when (state) {
            STATE_RIDING -> view?.askForArrive()
            STATE_BEGIN_DELIVERY -> Observable.timer(1, TimeUnit.SECONDS)
                    .doOnSubscribe { view?.showLoading() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate { view?.hideLoading() }
                    .subscribe {
                        view?.goToDeliveryMode()
                        state = STATE_DELIVERY
                    }
            STATE_DELIVERY -> view?.askForDeliveryArrive()
        }
    }

    fun customerRated() {
        Observable.timer(1, TimeUnit.SECONDS)
                .doOnSubscribe { view?.showLoading() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe { view?.deliveryFinished() }
    }

    fun driverDeliveryCanceled() {
        Observable.timer(1, TimeUnit.SECONDS)
                .doOnSubscribe { view?.showLoading() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe { view?.deliveryFinished() }
    }

    fun driverBeginDeliveryCanceled() {
        Observable.timer(1, TimeUnit.SECONDS)
                .doOnSubscribe { view?.showLoading() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe { view?.deliveryFinished() }
    }

    fun driverCanceled() {
        Observable.timer(1, TimeUnit.SECONDS)
                .doOnSubscribe { view?.showLoading() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { view?.hideLoading() }
                .subscribe { view?.deliveryFinished() }
    }
}
package ru.snipe.snipedriver.ui.driver_mode

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

private const val STATE_RIDING: Int = 0
private const val STATE_BEGIN_DELIVERY: Int = 1
private const val STATE_DELIVERY: Int = 2

@InjectViewState
class DriverPresenter() : MvpPresenter<DriverView>() {
  var currentState = STATE_RIDING

  fun driverArrived() {
    currentState = STATE_BEGIN_DELIVERY
    Observable.timer(1, TimeUnit.SECONDS)
      .doOnSubscribe { viewState.showLoading() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribe { viewState.goToBeginDeliveryMode() }
  }

  fun driverDeliveryArrived() {
    Observable.timer(1, TimeUnit.SECONDS)
      .doOnSubscribe { viewState.showLoading() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribe { viewState.goToRatingScreen() }
  }

  fun moveToNextState() {
    when (currentState) {
      STATE_RIDING -> viewState.askForArrive()
      STATE_BEGIN_DELIVERY -> Observable.timer(1, TimeUnit.SECONDS)
        .doOnSubscribe { viewState.showLoading() }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate { viewState.hideLoading() }
        .subscribe {
          viewState.goToDeliveryMode()
          currentState = STATE_DELIVERY
        }
      STATE_DELIVERY -> viewState.askForDeliveryArrive()
    }
  }

  fun customerRated() {
    Observable.timer(1, TimeUnit.SECONDS)
      .doOnSubscribe { viewState.showLoading() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribe { viewState.deliveryFinished() }
  }

  fun driverDeliveryCanceled() {
    Observable.timer(1, TimeUnit.SECONDS)
      .doOnSubscribe { viewState.showLoading() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribe { viewState.deliveryFinished() }
  }

  fun driverBeginDeliveryCanceled() {
    Observable.timer(1, TimeUnit.SECONDS)
      .doOnSubscribe { viewState.showLoading() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribe { viewState.deliveryFinished() }
  }

  fun driverCanceled() {
    Observable.timer(1, TimeUnit.SECONDS)
      .doOnSubscribe { viewState.showLoading() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribe { viewState.deliveryFinished() }
  }
}
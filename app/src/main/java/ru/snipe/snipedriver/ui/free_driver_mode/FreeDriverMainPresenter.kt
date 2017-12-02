package ru.snipe.snipedriver.ui.free_driver_mode

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.snipe.snipedriver.network.DataManager
import ru.snipe.snipedriver.ui.base_mvp.MoxyRxPresenter
import java.util.concurrent.TimeUnit

@InjectViewState
class FreeDriverMainPresenter(private val dataManager: DataManager) : MoxyRxPresenter<FreeDriverMainView>() {
  private var currentStatus: Boolean = false

  override fun attachView(view: FreeDriverMainView) {
    super.attachView(view)

    dataManager.getStatus()
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext {
        if (it) Observable.timer(2, TimeUnit.SECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe { viewState.driveRequest() }
      }
      .subscribeP({
        viewState.setStatus(it)
        currentStatus = it
      })
  }

  fun statusClicked() {
    Observable.just(!currentStatus)
      .doOnNext { viewState.showLoading() }
      .concatMap { dataManager.setStatus(it).toObservable<Any>() }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnTerminate { viewState.hideLoading() }
      .subscribeP({})
  }

  fun requestAccepted() {
    viewState.goToDriverMode()
  }
}
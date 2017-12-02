package ru.snipe.snipedriver.ui.base_mvp

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.snipe.snipedriver.utils.subscribeD
import timber.log.Timber

/**
 * Base RxPresenter for BaseMvpController which supports subscriptions with RxJava2
 *
 * NOTE: All inherited presenters should be annotated with @InjectViewState annotation
 * otherwise Moxy couldn't attach View to the presenter
 */
abstract class MoxyRxPresenter<View : MvpView> : MvpPresenter<View>() {
  /**
   * Holds a set of subscriptions local to this presenter
   */
  protected val presenterDisposables = CompositeDisposable()
  protected val viewDisposables = CompositeDisposable()
  /**
   * Contains a set of view events. They should be **reinitialize after view recreated**.
   * For example, in the [Controller.onAttach] method.
   * Otherwise subscription will expire on new attach.
   */
  var viewEvents = mapOf<Any, Observable<*>>()

  override fun attachView(view: View) {
    super.attachView(view)
    viewEvents.forEach { (key, observable) ->
      observable.subscribeV({ onViewEvent(observable, key, it) })
    }
  }

  /**
   * Notify about new view events from various destinations
   *
   * @see BaseControllerEvent
   */
  open fun onViewEvent(eventSource: Observable<*>, eventType: Any, eventValue: Any) { }

  override fun onDestroy() {
    presenterDisposables.clear()
    super.onDestroy()
  }

  /**
   * Clear all view disposables to prevent view leaks
   */
  override fun detachView(view: View) {
    viewDisposables.clear()
    viewEvents = emptyMap()
    super.detachView(view)
  }

  /**
   * Subscribes presenter to an observable with view lifecycle.
   * **This method should be used for example when using RxBinding utilities, when View managed by
   * this presenter provides an Observable(s) of user generated events instead of usual listener stuff.**
   *
   * Subscription will be lifecycle-managed and will be unsubscribed when [detachView] will be called
   * (it can become unsubscribed earlier if observable finishes)
   */
  fun <T> Observable<T>.subscribeV(onNext: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onNext, onError, viewDisposables)

  /**
   * @see [subscribeV]
   */
  fun <T> Single<T>.subscribeV(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onSuccess, onError, viewDisposables)

  /**
   * @see [subscribeV]
   */
  fun <T> Maybe<T>.subscribeV(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onSuccess, onError, viewDisposables)

  /**
   * @see [subscribeV]
   */
  fun Completable.subscribeV(onSuccess: () -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onSuccess, onError, viewDisposables)

  /**
   * Subscribes presenter to an observable with presenter lifecycle.
   *
   * Subscription will be lifecycle-managed and will be unsubscribed when [onDestroy] will be called
   * (it can become unsubscribed earlier if observable finishes)
   */
  fun <T> Observable<T>.subscribeP(onNext: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onNext, onError, presenterDisposables)

  /**
   * @see [subscribeP]
   */
  fun <T> Single<T>.subscribeP(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onSuccess, onError, presenterDisposables)

  /**
   * @see [subscribeP]
   */
  fun <T> Maybe<T>.subscribeP(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onSuccess, onError, presenterDisposables)

  /**
   * @see [subscribeP]
   */
  fun Completable.subscribeP(onSuccess: () -> Unit, onError: ((Throwable) -> Unit) = Timber::e)
    = this.subscribeD(onSuccess, onError, presenterDisposables)

  @Suppress("UNCHECKED_CAST")
  protected fun <T> viewEventSource(event: Any): Observable<T>
    = viewEvents[event] as? Observable<T> ?: throw ClassCastException("Maybe event $event didn't " +
    "defined for this presenter or tried to use before view attached")
}
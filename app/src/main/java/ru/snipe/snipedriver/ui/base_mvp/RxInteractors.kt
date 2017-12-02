package ru.snipe.snipedriver.ui.base_mvp

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private val DEFAULT_SINGLE_INTERACTOR_SCHEDULERS = SingleTransformer<Any, Any> { o ->
  o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> getDefaultSingleSchedulingTransformer(): SingleTransformer<T, T> {
  @Suppress("UNCHECKED_CAST")
  return DEFAULT_SINGLE_INTERACTOR_SCHEDULERS as SingleTransformer<T, T>
}

private val DEFAULT_COMPLETABLE_INTERACTOR_SCHEDULERS = CompletableTransformer { o ->
  o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

private val DEFAULT_OBSERVABLE_INTERACTOR_SCHEDULERS = ObservableTransformer<Any, Any> { o ->
  o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> getDefaultObservableSchedulingTransformer(): ObservableTransformer<T, T> {
  @Suppress("UNCHECKED_CAST")
  return DEFAULT_OBSERVABLE_INTERACTOR_SCHEDULERS as ObservableTransformer<T, T>
}

interface SingleRxInteractor<in T, R> {
  fun getSingle(params: T): Single<R>
}

interface CompletableRxInteractor<in T> {
  fun getCompletable(params: T): Completable
}

interface ObservableRxInteractor<in T, R> {
  fun getObservable(params: T): Observable<R>
}

/**
 * A base class for rx interactors.
 *
 * Contains methods for scheduling singles to play well with application lifecycle
 */
abstract class BaseSingleRxInteractor<T, R>(private val appTaskScheduler: AppTaskScheduler) : SingleRxInteractor<T, R> {
  open var schedulingTransformer: SingleTransformer<R, R> = getDefaultSingleSchedulingTransformer()

  /**
   * Prepares an singles of operation represented by this interactor, configures it to run
   * on corresponding Schedulers and ensures that it will be run only after application is initialized.
   */
  final override fun getSingle(params: T): Single<R> {
    return createSingle(params)
      .compose(appTaskScheduler.scheduleSingleAfterAppInit<R>())
      .compose(schedulingTransformer)
  }

  protected abstract fun createSingle(params: T): Single<R>
}

/**
 * A base class for rx interactors.
 * A convenience version of [BaseSingleRxInteractor] for cases where interactor's operation doesn't need any parameters
 *
 * Contains methods for scheduling singles to play well with application lifecycle
 */
abstract class BaseSingleRxInteractor0<R>(appTaskScheduler: AppTaskScheduler) : BaseSingleRxInteractor<Unit, R>(appTaskScheduler) {
  /**
   * Prepares an observable of operation represented by this interactor, configures it to run
   * on corresponding Schedulers and ensures that it will be run only after application is initialized.
   */
  fun getSingle(): Single<R> = super.getSingle(Unit)

  final override fun createSingle(params: Unit): Single<R> {
    return createSingle()
  }

  protected abstract fun createSingle(): Single<R>
}

/**
 * A base class for rx interactors.
 *
 * Contains methods for scheduling completable to play well with application lifecycle
 */
abstract class BaseCompletableRxInteractor<in T>(private val appTaskScheduler: AppTaskScheduler) : CompletableRxInteractor<T> {
  open var schedulingTransformer: CompletableTransformer = DEFAULT_COMPLETABLE_INTERACTOR_SCHEDULERS

  /**
   * Prepares an completable of operation represented by this interactor, configures it to run
   * on corresponding Schedulers and ensures that it will be run only after application is initialized.
   */
  override fun getCompletable(params: T): Completable {
    return createCompletable(params)
      .compose(appTaskScheduler.scheduleCompletableAfterAppInit())
      .compose(schedulingTransformer)
  }

  protected abstract fun createCompletable(params: T): Completable
}

/**
 * A base class for rx interactors.
 *
 * Contains methods for scheduling observables to play well with application lifecycle
 */
abstract class BaseObservableRxInteractor<T, R>(private val appTaskScheduler: AppTaskScheduler) : ObservableRxInteractor<T, R> {
  open var schedulingTransformer: ObservableTransformer<R, R> = getDefaultObservableSchedulingTransformer()

  /**
   * Prepares an observables of operation represented by this interactor, configures it to run
   * on corresponding Schedulers and ensures that it will be run only after application is initialized.
   */
  final override fun getObservable(params: T): Observable<R> {
    return createObservable(params)
      .compose(appTaskScheduler.scheduleObservableAfterAppInit<R>())
      .compose(schedulingTransformer)
  }

  protected abstract fun createObservable(params: T): Observable<R>
}

/**
 * A base class for rx interactors.
 * A convenience version of [BaseObservableRxInteractor] for cases where interactor's operation doesn't need any parameters
 *
 * Contains methods for scheduling observables to play well with application lifecycle
 */
abstract class BaseObservableRxInteractor0<R>(appTaskScheduler: AppTaskScheduler) : BaseObservableRxInteractor<Unit, R>(appTaskScheduler) {
  /**
   * Prepares an observables of operation represented by this interactor, configures it to run
   * on corresponding Schedulers and ensures that it will be run only after application is initialized.
   */
  fun getObservable(): Observable<R> = super.getObservable(Unit)

  final override fun createObservable(params: Unit): Observable<R> {
    return createObservable()
  }

  protected abstract fun createObservable(): Observable<R>
}
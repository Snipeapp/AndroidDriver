package ru.snipe.snipedriver.utils

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.subscribeD(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
  val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
  compositeDisposable.add(disposable)
  return disposable
}

fun <T> Single<T>.subscribeD(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
  val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
  compositeDisposable.add(disposable)
  return disposable
}

fun <T> Maybe<T>.subscribeD(onSuccess: (T) -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
  val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
  compositeDisposable.add(disposable)
  return disposable
}

fun Completable.subscribeD(onSuccess: () -> Unit, onError: ((Throwable) -> Unit), compositeDisposable: CompositeDisposable): Disposable {
  val disposable = this.observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onError)
  compositeDisposable.add(disposable)
  return disposable
}
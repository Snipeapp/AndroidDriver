package ru.snipe.snipedriver.ui.base_mvp

import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

interface AppTaskScheduler {
  fun <T> scheduleSingleAfterAppInit(): SingleTransformer<T, T>
  fun scheduleCompletableAfterAppInit(): CompletableTransformer
  fun <T> scheduleObservableAfterAppInit(): ObservableTransformer<T, T>
}

/**
 * Ensures that Singles run after application init has been performed
 */
class AppInitTaskScheduler(private val appInitEvent: Completable) : AppTaskScheduler {
  override fun <T> scheduleSingleAfterAppInit(): SingleTransformer<T, T> {
    return SingleTransformer { appInitEvent.andThen(it) }
  }
  override fun scheduleCompletableAfterAppInit(): CompletableTransformer {
    return CompletableTransformer { appInitEvent.andThen(it) }
  }
  override fun <T> scheduleObservableAfterAppInit(): ObservableTransformer<T, T> {
    return ObservableTransformer { appInitEvent.andThen(it) }
  }
}
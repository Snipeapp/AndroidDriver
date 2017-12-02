package ru.snipe.snipedriver

import android.app.Application
import android.os.StrictMode
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.snipe.snipedriver.ui.base_mvp.AppInitTaskScheduler
import ru.snipe.snipedriver.utils.AppConfig
import timber.log.Timber

abstract class BaseApplication<C : BaseApplicationComponent>(private val appConfig: AppConfig) : Application() {
  protected lateinit var appComponent: C
  private val appInitEvent = BehaviorSubject.create<Any>()

  /**
   * Creates an application component with global dependencies
   */
  abstract fun createComponent(): C

  /**
   * Performs application initialization.
   *
   * If any error occurs during initialization some exception should be thrown
   */
  abstract fun initializeApplication(applicationComponent: C): Single<Any>

  override fun onCreate() {
    super.onCreate()
    appComponent = createComponent()
    if (!appConfig.isReleaseBuild) {
      Timber.plant(Timber.DebugTree())
      setupStrictMode()
    }
    performApplicationInitialization(appComponent)
  }

  private fun performApplicationInitialization(appComponent: C) {
    initializeApplication(appComponent)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        {
          appInitEvent.onNext(true)
          Timber.d("application successfully initialized")
        },
        {
          Timber.d("application initialization failed")
        }
      )
  }

  private fun setupStrictMode() {
    // penaltyDeath() cause to app fall on firebase bug
    StrictMode.setThreadPolicy(
      StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build())
    StrictMode.setVmPolicy(
      StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build())
  }

  /**
   * Returns an completable when application initialization is finished.
   * If _subscribe()_ is called after initialization is finished, value will be emitted immediately.
   */
  fun appInitEvent(): Completable {
    return appInitEvent.firstOrError().toCompletable()
  }
}

interface BaseApplicationComponent {
  fun appInitTaskScheduler(): AppInitTaskScheduler
}
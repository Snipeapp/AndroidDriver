package ru.snipe.snipedriver

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.fabric.sdk.android.Fabric
import io.reactivex.Single
import ru.snipe.snipedriver.utils.AppConfig

fun Context.getApplication(): MyApplication {
  return this.applicationContext as MyApplication
}

fun Context.getAppComponent(): ApplicationComponent {
  return this.getApplication().getApplicationComponent()
}

class MyApplication : BaseApplication<ApplicationComponent>(AppConfig(isReleaseBuild = !BuildConfig.DEBUG)) {
  var refWatcher: RefWatcher? = null

  override fun onCreate() {
    super.onCreate()
    setupLeakAnalysis()
    Fabric.with(this, Crashlytics())
  }

  private fun setupLeakAnalysis() {
    refWatcher = LeakCanary.install(this)
  }

  override fun createComponent(): ApplicationComponent {
    return DaggerApplicationComponent.builder()
      .applicationModule(ApplicationModule(this))
      .build()
  }

  override fun initializeApplication(applicationComponent: ApplicationComponent): Single<Any> {
    return Single.fromCallable {}
  }

  fun getApplicationComponent(): ApplicationComponent {
    return appComponent
  }
}
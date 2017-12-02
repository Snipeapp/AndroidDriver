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

fun Context.getAppComponent(): AppComponent {
  return this.getApplication().getApplicationComponent()
}

class MyApplication : BaseApplication<AppComponent>(AppConfig(isReleaseBuild = !BuildConfig.DEBUG)) {
  var refWatcher: RefWatcher? = null

  override fun onCreate() {
    super.onCreate()
    setupLeakAnalysis()
    Fabric.with(this, Crashlytics())
  }

  private fun setupLeakAnalysis() {
    refWatcher = LeakCanary.install(this)
  }

  override fun createComponent(): AppComponent {
    return DaggerAppComponent.builder()
      .appModule(AppModule(this))
      .build()
  }

  override fun initializeApplication(applicationComponent: AppComponent): Single<Any> {
    return Single.fromCallable {}
  }

  fun getApplicationComponent(): AppComponent {
    return appComponent
  }
}
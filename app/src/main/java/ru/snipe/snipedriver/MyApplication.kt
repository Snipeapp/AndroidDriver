package ru.snipe.snipedriver

import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
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
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
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

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  fun getApplicationComponent(): AppComponent {
    return appComponent
  }
}
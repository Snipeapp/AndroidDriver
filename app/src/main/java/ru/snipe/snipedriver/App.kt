package ru.snipe.snipedriver

import android.app.Application
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class App : Application() {
  lateinit var component: AppComponent

  override fun onCreate() {
    super.onCreate()
    MultiDex.install(this)

    component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    Fabric.with(this, Crashlytics())
  }
}

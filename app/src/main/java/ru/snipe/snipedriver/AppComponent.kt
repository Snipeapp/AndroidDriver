package ru.snipe.snipedriver

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import ru.snipe.snipedriver.annotations.ApplicationScope
import ru.snipe.snipedriver.ui.base_mvp.AppInitTaskScheduler
import ru.snipe.snipedriver.ui.driver_mode.DriverComponent
import ru.snipe.snipedriver.ui.driver_mode.DriverModule
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverMainComponent
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverMainModule
import ru.snipe.snipedriver.ui.onboarding.OnBoardingComponent
import ru.snipe.snipedriver.ui.onboarding.OnBoardingModule
import ru.snipe.snipedriver.ui.phone_number.PhoneNumberComponent
import ru.snipe.snipedriver.ui.phone_number.PhoneNumberModule
import ru.snipe.snipedriver.ui.verify_code.VerifyCodeComponent
import ru.snipe.snipedriver.ui.verify_code.VerifyCodeModule

@ApplicationScope
@Component(modules = arrayOf(AppModule::class))
interface AppComponent : BaseApplicationComponent {
  fun plusOnBoardingComponent(module: OnBoardingModule): OnBoardingComponent
  fun plusDriverComponent(module: DriverModule): DriverComponent
  fun plusFreeDriverMainComponent(module: FreeDriverMainModule): FreeDriverMainComponent
  fun plusPhoneNumberComponent(module: PhoneNumberModule): PhoneNumberComponent
  fun plusVerifyCodeComponent(module: VerifyCodeModule): VerifyCodeComponent
}

@Module
class AppModule(private val application: MyApplication) {
  @Provides
  @ApplicationScope
  fun provideContext(): Context = application

  @Provides
  fun provideAppInitTaskScheduler(): AppInitTaskScheduler {
    return AppInitTaskScheduler(application.appInitEvent())
  }

  @Provides
  @ApplicationScope
  fun provideConnectivityEvents(context: Context): Observable<Connectivity> {
    return ReactiveNetwork.observeNetworkConnectivity(context)
  }
}
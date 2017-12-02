package ru.snipe.snipedriver

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.snipe.snipedriver.view.driver_mode.DriverActivity
import ru.snipe.snipedriver.view.driver_mode.DriverFragment
import ru.snipe.snipedriver.view.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.view.onboarding.OnBoardingFragment
import ru.snipe.snipedriver.view.phone_number.PhoneNumberFragment
import ru.snipe.snipedriver.view.verify_code.VerifyCodeFragment
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(phoneNumberFragment: PhoneNumberFragment)
    fun inject(onBoardingFragment: OnBoardingFragment)
    fun inject(verifyCodeFragment: VerifyCodeFragment)
    fun inject(freeDriverActivity: FreeDriverActivity)
    fun inject(driverActivity: DriverActivity)
    fun inject(driverFragment: DriverFragment)
}

@Module
class AppModule(private var context: Context) {
    @Singleton
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun network(context: Context) =
      ReactiveNetwork.observeNetworkConnectivity(context)
}
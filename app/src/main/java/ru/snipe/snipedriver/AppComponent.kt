package ru.snipe.snipedriver

import android.content.Context
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
import dagger.Component
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava.interop.RxJavaInterop
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
}

@Module
class AppModule(private var context: Context) {
    @Singleton
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun network(context: Context) =
            RxJavaInterop.toV2Observable(ReactiveNetwork.observeNetworkConnectivity(context))
}
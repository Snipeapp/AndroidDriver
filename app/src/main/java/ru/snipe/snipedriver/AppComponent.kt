package ru.snipe.snipedriver

import dagger.Component
import ru.snipe.snipedriver.view.onboarding.OnBoardingFragment
import ru.snipe.snipedriver.view.phone_number.PhoneNumberFragment
import ru.snipe.snipedriver.view.verify_code.VerifyCodeFragment
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun inject(phoneNumberFragment: PhoneNumberFragment)
    fun inject(onBoardingFragment: OnBoardingFragment)
    fun inject(verifyCodeFragment: VerifyCodeFragment)
}
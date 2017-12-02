package ru.snipe.snipedriver.ui.phone_number

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.snipe.snipedriver.annotations.FragmentScope
import ru.snipe.snipedriver.network.DataManager

@FragmentScope
@Subcomponent(modules = arrayOf(PhoneNumberModule::class))
interface PhoneNumberComponent {
  fun presenter(): PhoneNumberPresenter
}

@Module
class PhoneNumberModule() {
  @FragmentScope
  @Provides
  fun providePresenter(dataManager: DataManager): PhoneNumberPresenter {
    return PhoneNumberPresenter(dataManager)
  }
}
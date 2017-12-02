package ru.snipe.snipedriver.ui.driver_mode

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.snipe.snipedriver.annotations.FragmentScope

@FragmentScope
@Subcomponent(modules = arrayOf(DriverModule::class))
interface DriverComponent {
  fun presenter(): DriverPresenter
}

@Module
class DriverModule {
  @FragmentScope
  @Provides
  fun providePresenter(): DriverPresenter {
    return DriverPresenter()
  }
}
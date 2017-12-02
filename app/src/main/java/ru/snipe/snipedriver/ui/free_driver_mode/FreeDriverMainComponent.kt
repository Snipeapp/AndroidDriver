package ru.snipe.snipedriver.ui.free_driver_mode

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.snipe.snipedriver.annotations.FragmentScope
import ru.snipe.snipedriver.network.DataManager

@FragmentScope
@Subcomponent(modules = arrayOf(FreeDriverMainModule::class))
interface FreeDriverMainComponent {
  fun presenter(): FreeDriverMainPresenter
}

@Module
class FreeDriverMainModule {
  @FragmentScope
  @Provides
  fun providePresenter(dataManager: DataManager): FreeDriverMainPresenter {
    return FreeDriverMainPresenter(dataManager)
  }
}
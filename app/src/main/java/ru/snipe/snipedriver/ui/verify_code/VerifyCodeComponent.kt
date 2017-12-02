package ru.snipe.snipedriver.ui.verify_code

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.snipe.snipedriver.annotations.FragmentScope
import ru.snipe.snipedriver.network.DataManager

@FragmentScope
@Subcomponent(modules = arrayOf(VerifyCodeModule::class))
interface VerifyCodeComponent {
  fun presenter(): VerifyCodePresenter
}

@Module
class VerifyCodeModule(private val phone: String) {
  @FragmentScope
  @Provides
  fun providePresenter(dataManager: DataManager): VerifyCodePresenter {
    return VerifyCodePresenter(dataManager, phone)
  }
}
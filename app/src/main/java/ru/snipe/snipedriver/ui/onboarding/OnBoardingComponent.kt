package ru.snipe.snipedriver.ui.onboarding

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Observable
import ru.snipe.snipedriver.annotations.FragmentScope
import ru.snipe.snipedriver.network.DataManager

@FragmentScope
@Subcomponent(modules = arrayOf(OnBoardingModule::class))
interface OnBoardingComponent {
  fun presenter(): OnBoardingPresenter
}

@Module
class OnBoardingModule {
  @FragmentScope
  @Provides
  fun providePresenter(dataManager: DataManager,
                       networkChangeEvents: Observable<Connectivity>): OnBoardingPresenter {
    return OnBoardingPresenter(dataManager, networkChangeEvents)
  }
}
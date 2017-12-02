package ru.snipe.snipedriver.ui.base_mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.snipe.snipedriver.utils.EmptyViewConfig
import ru.snipe.snipedriver.utils.ErrorViewConfig

/**
 * Base ElceView interface to provide Error, Loading, Content and Empty states
 * With Moxy strategy [StateStrategyType] states will replace each other
 *
 * NOTE: All inherited interfaces should be rooted,
 * otherwise Moxy won't import them in the generated classes
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ElceView<in D> : MvpView {
  fun showContent(data: D)
  fun switchToContentState()
  fun switchToEmptyState(empty: EmptyViewConfig? = null)
  fun switchToErrorState(error: ErrorViewConfig)
  fun switchToLoadingState()
}
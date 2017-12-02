package ru.snipe.snipedriver.ui.base_mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpDelegate
import ru.snipe.snipedriver.ui.base.BaseFragment
import ru.snipe.snipedriver.utils.EmptyViewConfig
import ru.snipe.snipedriver.utils.ErrorViewConfig

/**
 * Date: 19-Dec-15
 * Time: 13:25
 *
 * @author Alexander Blinov
 * @author Yuri Shmakov
 * @author Konstantin Tckhovrebov
 */
abstract class BaseMvpFragment<in DATA> : BaseFragment(), ElceView<DATA> {
  private val mvpDelegate: MvpDelegate<Fragment> by lazy { MvpDelegate<Fragment>(this) }
  private var mIsStateSaved: Boolean = false

  override fun showContent(data: DATA) {}
  override fun switchToContentState() {}
  override fun switchToEmptyState(empty: EmptyViewConfig?) {}
  override fun switchToErrorState(error: ErrorViewConfig) {}
  override fun switchToLoadingState() {}

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mvpDelegate.onCreate(savedInstanceState)
  }

  override fun onStart() {
    super.onStart()
    mIsStateSaved = false
    mvpDelegate.onAttach()
  }

  override fun onResume() {
    super.onResume()
    mIsStateSaved = false
    mvpDelegate.onAttach()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mIsStateSaved = true
    mvpDelegate.onSaveInstanceState(outState)
    mvpDelegate.onDetach()
  }

  override fun onStop() {
    super.onStop()
    mvpDelegate.onDetach()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mvpDelegate.onDetach()
    mvpDelegate.onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()

    //We leave the screen and respectively all fragments will be destroyed
    if (activity!!.isFinishing) {
      mvpDelegate.onDestroy()
      return
    }

    // When we rotate device isRemoving() return true for fragment placed in backstack
    // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
    if (mIsStateSaved) {
      mIsStateSaved = false
      return
    }

    // See https://github.com/Arello-Mobile/Moxy/issues/24
    var anyParentIsRemoving = false
    var parent = parentFragment
    while (!anyParentIsRemoving && parent != null) {
      anyParentIsRemoving = parent.isRemoving
      parent = parent.parentFragment
    }

    if (isRemoving || anyParentIsRemoving) {
      mvpDelegate.onDestroy()
    }
  }
}
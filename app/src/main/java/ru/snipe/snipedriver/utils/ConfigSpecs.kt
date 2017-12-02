package ru.snipe.snipedriver.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.view.View
import ru.snipe.snipedriver.R

data class TitleConfig(private val fromStringText: String? = null,
                       @StringRes private val fromStringRes: Int = 0) {

  fun resolveTitle(context: Context): String {
    return fromStringText ?: if (fromStringRes != 0) context.getString(fromStringRes) else ""
  }
}

data class ContentConfig(
  /**
   * A layout resource used by this controller content.
   */
  @LayoutRes
  val contentLayoutResId: Int = 0,
  /**
   * Specifies an toolbar title
   */
  val titleConfig: TitleConfig? = null,
  /**
   * Specifies navigation icon in toolbar
   */
  val navigationIcon: NavigationIconType = NavigationIconType.None,
  /**
   * Specifies layout to inflate in toolbar
   */
  @LayoutRes
  val internalView: Int = View.NO_ID,
  /**
   * Specifies layout to inflate in AppBarLayout
   */
  @LayoutRes
  val additionalView: Int = View.NO_ID,
  @MenuRes
  val menu: Int = 0,
  @StyleRes
  val themeStyleRes: Int = R.style.AppTheme,
  val hideAppBar: Boolean = false,
  val fillStatusBarThemeColor: Boolean = true
)

enum class NavigationIconType {
  None,
  Menu,
  Back,
  Close
}

data class AppConfig(
  val isReleaseBuild: Boolean
)
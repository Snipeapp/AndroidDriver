package ru.snipe.snipedriver.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.TextView

data class ErrorViewConfig(
  @DrawableRes
  val drawableRes: Int = 0,
  @StringRes
  val titleTextRes: Int = 0,
  val titleText: CharSequence? = null,
  val titleParams: List<Any>? = null,
  @StringRes
  val actionTextRes: Int = 0,
  val mainAction: (() -> Unit)? = null,
  val returnAction: (() -> Unit)? = null,
  val showIcon: Boolean = true,
  val showThemedActionColor: Boolean = true) {

  fun resolveTitle(context: Context): CharSequence {
    return if (titleTextRes == 0) titleText!! else {
      if (titleParams == null) context.getString(titleTextRes) else context.getString(titleTextRes, *titleParams.toTypedArray())
    }
  }
}

data class EmptyViewConfig(@StringRes val titleTextRes: Int,
                           @DrawableRes val iconRes: Int = 0,
                           @ColorRes val titleColorRes: Int = 0)

class EmptyView : TextView {
  var content: EmptyViewConfig? = null
    set(value) {
      field = value
      if (value != null) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(0, value.iconRes, 0, 0)
        setText(value.titleTextRes)
        if (value.titleColorRes != 0) setTextColor(ContextCompat.getColor(context, value.titleColorRes))
      }
    }

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
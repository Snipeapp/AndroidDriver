package ru.snipe.snipedriver.utils

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleableRes
import android.support.v4.content.res.ResourcesCompat

@ColorInt
fun Resources.getColorCompat(@ColorRes colorRes: Int, theme: Resources.Theme? = null): Int {
  return ResourcesCompat.getColor(this, colorRes, theme)
}

fun Resources.getDrawableCompat(@DrawableRes drawableRes: Int, theme: Resources.Theme? = null): Drawable? {
  return ResourcesCompat.getDrawable(this, drawableRes, theme)
}

fun TypedArray.getResOrNothing(@StyleableRes attrId: Int): Int? {
  val res = this.getResourceId(attrId, 0)
  return if (res != 0) res else null
}

fun Resources.dpToPx(dp: Int) = (this.displayMetrics.density * dp + 0.5f).toInt()
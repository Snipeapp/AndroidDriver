package ru.snipe.snipedriver.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import java.io.InvalidObjectException

fun Context.layoutInflater(): LayoutInflater {
  return LayoutInflater.from(this)
}

@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
  return ContextCompat.getColor(this, colorRes)
}

fun Context.getDisplayWidthPx(fromResources: Boolean = true): Int {
  if (fromResources) {
    val metrics = resources.displayMetrics
    return metrics.widthPixels
  } else {
    val metrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
  }
}

fun Context.getThemeColor(@AttrRes attr: Int): Int
  = getThemeAttr(attr, { it.getColor(0, 0) })

private inline fun <T> Context.getThemeAttr(@AttrRes attr: Int, extractor: (TypedArray) -> T): T {
  val attrs = intArrayOf(attr)
  val typedArray = this.obtainStyledAttributes(attrs)
  val value: T = extractor(typedArray)
  typedArray.recycle()
  return value
}

@SuppressLint("ObsoleteSdkInt")
fun Activity.setStatusBarColorCompat(@ColorInt colorRes: Int) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    this.window.statusBarColor = colorRes
  }
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int,
                              theme: Resources.Theme? = null): Drawable? {
  return ResourcesCompat.getDrawable(this.resources, drawableRes, theme)
}

fun Context.dpToPx(dp: Int) = this.resources.dpToPx(dp)

fun Int.asString(context: Context?,
                 vararg params: Any = emptyArray()): String {
  return context?.getString(this, *params)
    ?: throw InvalidObjectException("context shouldn't be null")
}

@ColorInt
fun Int.asColorInt(context: Context?): Int {
  return context?.getColorCompat(this) ?: throw InvalidObjectException("context shouldn't be null")
}
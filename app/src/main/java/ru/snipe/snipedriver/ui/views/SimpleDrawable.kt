package ru.snipe.snipedriver.ui.views;

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable

open class SimpleDrawable : Drawable() {
  override fun draw(canvas: Canvas?) {}
  override fun setAlpha(p0: Int) {}
  override fun getOpacity() = 0
  override fun setColorFilter(p0: ColorFilter?) {}
}
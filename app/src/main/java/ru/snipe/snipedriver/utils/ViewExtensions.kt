package ru.snipe.snipedriver.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.design.widget.TabLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView

fun View.layoutInflater(): LayoutInflater = this.context.layoutInflater()

var View.isVisible: Boolean
  get() = this.visibility == View.VISIBLE
  set(value) {
    this.visibility = if (value) View.VISIBLE else View.GONE
  }

fun RecyclerView.init(adapter: RecyclerView.Adapter<*>,
                      spanCount: Int = 0,
                      spanSizeLookup: GridLayoutManager.SpanSizeLookup,
                      orientation: Int = GridLayoutManager.VERTICAL) {
  val layoutManager = GridLayoutManager(this.context, spanCount, orientation, false)
  layoutManager.spanSizeLookup = spanSizeLookup
  this.layoutManager = layoutManager
  this.adapter = adapter
  this.animation = null
}

inline fun <reified T : View> View.findView(id: Int): T = findViewById(id) as T
inline fun <reified T : View> View.findViewOptional(id: Int): T? = findViewById(id) as? T

/**
 * Returns a first view matching predicate
 */
fun View.findView(predicate: (View) -> Boolean): View? {
  if (this is ViewGroup) {
    for (i in 0..this.childCount - 1) {
      if (predicate(this.getChildAt(i))) {
        return this.getChildAt(i)
      }
    }
  } else { // if (this is View)
    return if (predicate(this)) this else null
  }

  return null
}

fun Drawable.withTint(@ColorInt tint: Int): Drawable
  = mutate().apply { DrawableCompat.setTint(DrawableCompat.wrap(this), tint) }

fun ImageView.tintDrawableRes(@AttrRes colorRes: Int) {
  setImageDrawable(drawable.withTint(context.getColorCompat(colorRes)))
}

fun View.tintDrawableRes(@AttrRes colorInt: Int) {
  background.setColorFilter(colorInt, PorterDuff.Mode.SRC_IN)
}

inline fun View.runBeforeDraw(crossinline action: (View) -> Unit) {
  this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {

    override fun onPreDraw(): Boolean {
      viewTreeObserver.removeOnPreDrawListener(this)
      action.invoke(this@runBeforeDraw)
      return true
    }
  })
}

inline fun View.runOnLayout(isRemove: Boolean = false,
                            crossinline action: (View) -> Unit): ViewTreeObserver.OnGlobalLayoutListener {
  val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
    override fun onGlobalLayout() {
      if (isRemove) {
        viewTreeObserver.removeOnGlobalLayoutListener(this)
      }
      action.invoke(this@runOnLayout)
    }
  }
  this.viewTreeObserver.addOnGlobalLayoutListener(listener)
  return listener
}

private const val minSoftKeyboardHeight = 100
fun View.isKeyboardShown(measureRect: Rect): Pair<Boolean, Int> {
  this.getWindowVisibleDisplayFrame(measureRect)
  val heightDiff = this.rootView.bottom - measureRect.bottom
  val isKeyboardShown = heightDiff > minSoftKeyboardHeight * resources.displayMetrics.density
  return isKeyboardShown to heightDiff
}

private const val SCALE_Y_INVERSE = -1f
fun TabLayout.inverseIndicator() {
  // Hack to show indicator at top instead of bottom
  this.scaleY = SCALE_Y_INVERSE
  val tabsLayout = this.getChildAt(0) as ViewGroup
  (0 until tabsLayout.childCount)
    .map { tabsLayout.getChildAt(it) }
    .forEach { it.scaleY = SCALE_Y_INVERSE }
}

fun View.toBitmap(): Bitmap {
  val returnedBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_4444)
  val targetCanvas = Canvas(returnedBitmap)
  if (background != null)
    background.draw(targetCanvas)
  else
    targetCanvas.drawColor(Color.WHITE)
  this.draw(targetCanvas)
  return returnedBitmap
}
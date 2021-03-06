package ru.snipe.snipedriver.utils

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.SystemClock
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.design.widget.TabLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.views.SimpleDrawable

fun View.layoutInflater(): LayoutInflater = this.context.layoutInflater()

var View.isVisible: Boolean
  get() = this.visibility == View.VISIBLE
  set(value) {
    this.visibility = if (value) View.VISIBLE else View.GONE
  }

var View.isInvisible: Boolean
  get() = this.visibility == View.INVISIBLE
  set(value) {
    this.visibility = if (value) View.INVISIBLE else View.VISIBLE
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

fun SpannableStringBuilder.withSpans(text: String, vararg spans: Any): SpannableStringBuilder {
  val from = length
  append(text)
  spans.forEach { span -> setSpan(span, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
  return this
}

fun SpannableStringBuilder.appendSpace() = this.append(" ")

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

/**
 * Sets up a click listener so that it will be protected from multiple subsequent clicks which come very fast.
 * It will ignore clicks which are too close in time (1 second by default)
 */
fun View.setDebouncingOnClickListener(listener: ((View) -> Unit)?) {
  setOnClickListener(if (listener != null) DebouncingOnClickListener(targetListener = listener) else null)
}

private class DebouncingOnClickListener(private val targetListener: (View) -> Unit) : View.OnClickListener {
  private var lastClickTime = 0L
  override fun onClick(v: View) {
    if (SystemClock.elapsedRealtime() - lastClickTime < CLICK_DELAY_MS) {
      return // clicking too fast, not allowed
    }
    lastClickTime = SystemClock.elapsedRealtime()
    targetListener.invoke(v)
  }
}

fun View.showRipple(rippleSizeDp: Int) {
  val colorHighlight = context.getThemeColor(R.attr.colorControlHighlight)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    background = RippleDrawable(
      ColorStateList.valueOf(colorHighlight),
      null,
      object : SimpleDrawable() {

        private val paint = Paint()
        private val size = context.dpToPx(rippleSizeDp).toFloat()

        override fun draw(canvas: Canvas?) {
          canvas?.drawCircle(
            bounds.centerX().toFloat(),
            bounds.centerY().toFloat(),
            size,
            paint)
        }
      })
  } else {
    background = StateListDrawable().apply {
      addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(colorHighlight))
    }
  }
}
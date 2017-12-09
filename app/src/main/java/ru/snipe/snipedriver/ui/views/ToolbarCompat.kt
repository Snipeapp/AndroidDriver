package ru.snipe.snipedriver.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.*

class ToolbarCompat : RelativeLayout {
  @ColorInt
  var iconColor: Int = 0
    set(value) {
      field = value
      iconView.setImageDrawable(iconView.drawable.withTint(value))
    }

  var iconClickAction: ((View) -> Unit)? = null

  @ColorInt
  var titleColor: Int = 0
    set(value) {
      field = value
      titleView.setTextColor(value)
    }

  var titleText: String = ""
    set(value) {
      field = value
      titleView.text = titleText
    }

  @ColorInt
  var optionsColor: Int = 0
    set(value) {
      field = value
      titleView.setTextColor(value)
    }

  var optionsClickAction: ((View) -> Unit)? = null

  private val iconView: ImageView
  private val titleView: TextView
  private val optionsView: TextView

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    context.layoutInflater().inflate(R.layout.layout_toolbar, this, true)
    iconView = findView(R.id.toolbar_icon)
    titleView = findView(R.id.toolbar_title)
    optionsView = findView(R.id.toolbar_options)
    initView(context)
  }

  private fun initView(context: Context) {
    val colorPrimary = context.getThemeColor(R.attr.colorPrimary)
    val titleTextColor = context.getThemeColor(R.attr.titleTextColor)
    val toolbarColor = context.getThemeColor(R.attr.toolbarBarColor)

    iconColor = colorPrimary
    titleColor = titleTextColor
    optionsColor = colorPrimary
    titleText = R.string.app_name.asString(context)
    iconView.setOnClickListener { iconClickAction?.invoke(it) }
    optionsView.setOnClickListener { optionsClickAction?.invoke(it) }
    setBackgroundColor(toolbarColor)
    initRipple()
  }

  private fun initRipple() {
    val colorHighlight = context.getThemeColor(R.attr.colorControlHighlight)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      iconView.background = RippleDrawable(
        ColorStateList.valueOf(colorHighlight),
        null,
        object : SimpleDrawable() {

          private val paint = Paint()
          private val size = context.dpToPx(20).toFloat()

          override fun draw(canvas: Canvas?) {
            canvas?.drawCircle(
              bounds.centerX().toFloat(),
              bounds.centerY().toFloat(),
              size,
              paint)
          }
        })
    } else {
      iconView.background = StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(colorHighlight))
      }
    }
  }
}
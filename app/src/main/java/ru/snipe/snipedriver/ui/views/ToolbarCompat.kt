package ru.snipe.snipedriver.ui.views

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.ListPopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.*

class ToolbarCompat : RelativeLayout {

  var titleText: String = ""
    set(value) {
      field = value
      titleView.text = titleText
    }

  @ColorInt
  var iconColor: Int = 0
    set(value) {
      field = value
      iconView.setImageDrawable(iconView.drawable.withTint(value))
    }

  @ColorInt
  var titleColor: Int = 0
    set(value) {
      field = value
      titleView.setTextColor(value)
    }

  @ColorInt
  var optionsColor: Int = 0
    set(value) {
      field = value
      titleView.setTextColor(value)
    }

  var iconClickAction: ((View) -> Unit)? = null

  var optionsClickAction: ((View) -> Unit)? = null

  var optionItem: OptionsItem? = null
    set(value) {
      field = value
      optionsView.isVisible = value != null
      optionsView.text = value?.optionTitle ?: ""
      val endDrawableRes = value?.optionImage ?: 0
      TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(optionsView, 0, 0, endDrawableRes, 0)
      optionsView.setDebouncingOnClickListener { value?.clickAction?.invoke(this, 0) }
    }

  var optionItems: List<OptionsItem>? = null
    set(value) {
      field = value
      optionsView.isVisible = value != null
      if (value == null) {
        optionsWindow = null
        optionsView.setOnClickListener(null)
      } else {
        optionsWindow = context.createListWindow(value.map { it.optionTitle }, optionsView, { view, _, position ->
          value[position].clickAction?.invoke(view, position)
        })
        optionsView.setDebouncingOnClickListener { optionsWindow?.show() }
      }
      val endDrawableRes = if (value == null) 0 else R.drawable.ic_more_vert_black
      TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(optionsView, 0, 0, endDrawableRes, 0)
      optionsView.text = ""
    }

  private var optionsWindow: ListPopupWindow? = null
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
    optionItem = null
    titleText = R.string.app_name.asString(context)
    iconView.setDebouncingOnClickListener { iconClickAction?.invoke(it) }
    optionsView.setDebouncingOnClickListener { optionsClickAction?.invoke(it) }
    setBackgroundColor(toolbarColor)
    iconView.showRipple(20)
    optionsView.showRipple(60)
  }
}
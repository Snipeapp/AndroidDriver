package ru.snipe.snipedriver.ui.views

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.*

class ToolbarCompat : RelativeLayout {

  var navigationType: NavigationIconType = NavigationIconType.None
    set(value) {
      field = value
      val drawableRes: Int? = when (value) {
        NavigationIconType.Back -> R.drawable.ic_arrow_back_black
        NavigationIconType.Close -> R.drawable.ic_close_black
        NavigationIconType.Menu -> R.drawable.ic_dehaze_black
        NavigationIconType.None -> null
      }
      drawableRes?.let { navigationView.setImageResource(it) }
      navigationView.isInvisible = drawableRes == null
      navigationView.showRipple(20)
    }

  var titleText: String = ""
    set(value) {
      field = value
      titleView.text = titleText
      titleView.isInvisible = titleText.isBlank()
    }

  @ColorInt
  var iconColor: Int = 0
    set(value) {
      field = value
      navigationView.setImageDrawable(navigationView.drawable.withTint(value))
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

  var navigationClickAction: ((View) -> Unit)? = null

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
        optionsMenu = null
        optionsView.setOnClickListener(null)
      } else {
        optionsMenu = context.createPopupMenu(value.map { it.optionTitle }, optionsView, { view, _, position ->
          value[position].clickAction?.invoke(view, position)
        })
        optionsView.setDebouncingOnClickListener { optionsMenu?.show() }
      }
      val endDrawableRes = if (value == null) 0 else R.drawable.ic_more_vert_black
      TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(optionsView, 0, 0, endDrawableRes, 0)
      optionsView.text = ""
    }

  private val navigationView: ImageView
  private val titleView: TextView
  private val optionsView: TextView
  private val elevation: View

  private var optionsMenu: PopupMenu? = null

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    context.layoutInflater().inflate(R.layout.layout_toolbar, this, true)
    navigationView = findView(R.id.toolbar_icon)
    titleView = findView(R.id.toolbar_title)
    optionsView = findView(R.id.toolbar_options)
    elevation = findView(R.id.toolbar_elevation)

    initView(context, attrs)
  }

  private fun initView(context: Context, attrs: AttributeSet?) {
    val colorPrimary = context.getThemeColor(R.attr.colorPrimary)
    val titleTextColor = context.getThemeColor(R.attr.titleTextColor)
    val toolbarColor = context.getThemeColor(R.attr.toolbarBarColor)

    iconColor = colorPrimary
    titleColor = titleTextColor
    optionsColor = colorPrimary
    setBackgroundColor(toolbarColor)

    titleText = R.string.app_name.asString(context)
    optionsView.showRipple(60)
    navigationView.setDebouncingOnClickListener { navigationClickAction?.invoke(it) }
    navigationType = NavigationIconType.Back

    context.obtainStyledAttributes(attrs, R.styleable.ToolbarCompat)?.run {
      val showNavigation = getBoolean(R.styleable.ToolbarCompat_tcShowNavigationIcon, true)
      val showTitle = getBoolean(R.styleable.ToolbarCompat_tcShowTitle, true)
      val showElevation = getBoolean(R.styleable.ToolbarCompat_tcShowElevation, true)
      navigationView.isVisible = showNavigation
      titleView.isVisible = showTitle
      elevation.isVisible = showElevation
      recycle()
    }
  }
}
package ru.snipe.snipedriver.ui.base

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import ru.snipe.snipedriver.utils.PropertyBinder
import ru.snipe.snipedriver.utils.UnbindableView
import ru.snipe.snipedriver.utils.bindView
import ru.snipe.snipedriver.utils.bindViewOpt
import kotlin.properties.ReadOnlyProperty

abstract class BaseActivity : AppCompatActivity(), UnbindableView {
  abstract var contentDelegate: ActivityContentDelegate
  override val propertyBinder = PropertyBinder()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(contentDelegate.config.contentLayoutResId)
    initView(this)
  }

  protected open fun initView(view: Activity) {
  }

  protected fun <VIEW_TYPE : View> bindView(@IdRes id: Int): ReadOnlyProperty<Activity, VIEW_TYPE>
    = bindView(propertyBinder, id)

  protected fun <VIEW_TYPE : View> bindViewOpt(@IdRes id: Int): ReadOnlyProperty<Activity, VIEW_TYPE?>
    = bindViewOpt(propertyBinder, id)
}
package ru.snipe.snipedriver.ui.base

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.*
import kotlin.properties.ReadOnlyProperty

abstract class BaseActivity<out T : Fragment> : AppCompatActivity(), UnbindableView {
  open var contentDelegate = ActivityContentDelegate(this,
    ContentConfig(R.layout.content_base))
  override val propertyBinder = PropertyBinder()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(contentDelegate.config.contentLayoutResId)
    if (savedInstanceState == null) {
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_container, createFragment(
          getFragment(),
          { putAll(intent?.extras ?: Bundle()) }
        ))
        .commit()
    }
  }

  protected open fun initView(view: View) {
  }

  abstract fun getFragment(): T

  protected fun <VIEW_TYPE : View> bindView(@IdRes id: Int): ReadOnlyProperty<Activity, VIEW_TYPE>
    = bindView(propertyBinder, id)

  protected fun <VIEW_TYPE : View> bindViewOpt(@IdRes id: Int): ReadOnlyProperty<Activity, VIEW_TYPE?>
    = bindViewOpt(propertyBinder, id)
}
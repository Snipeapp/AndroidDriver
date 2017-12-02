package ru.snipe.snipedriver.ui.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.snipe.snipedriver.utils.PropertyBinder
import ru.snipe.snipedriver.utils.UnbindableView
import ru.snipe.snipedriver.utils.bindView
import ru.snipe.snipedriver.utils.bindViewOpt
import kotlin.properties.ReadOnlyProperty

abstract class BaseFragment : Fragment(), UnbindableView {
  protected abstract val contentDelegate: ContentDelegate
  override val propertyBinder = PropertyBinder()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = contentDelegate.createView(inflater, container)
    propertyBinder.viewContainer = view
    initView(view)
    return view
  }

  override fun onDestroyView() {
    propertyBinder.reset()
    contentDelegate.onDestroyView()
    super.onDestroyView()
  }

  protected open fun initView(view: View) {
  }

  protected fun <VIEW_TYPE : View> bindView(@IdRes id: Int): ReadOnlyProperty<Fragment, VIEW_TYPE>
    = bindView(propertyBinder, id)
  protected fun <VIEW_TYPE : View> bindViewOpt(@IdRes id: Int): ReadOnlyProperty<Fragment, VIEW_TYPE?>
    = bindViewOpt(propertyBinder, id)
}
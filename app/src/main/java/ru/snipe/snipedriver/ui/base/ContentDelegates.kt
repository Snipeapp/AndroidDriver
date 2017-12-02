package ru.snipe.snipedriver.ui.base

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.snipe.snipedriver.utils.ContentConfig

open class FragmentContentDelegate(val fragment: Fragment, val config: ContentConfig) {
  open fun createView(inflater: LayoutInflater, parent: ViewGroup?): View {
    val view = inflater.inflate(config.contentLayoutResId, parent, false)
    return view
  }

  fun onDestroyView() {}
}

open class ActivityContentDelegate(val activity: Activity, val config: ContentConfig)
package ru.snipe.snipedriver.ui.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.createFragment

abstract class BaseContentActivity<out T : Fragment> : BaseActivity() {
  override var contentDelegate = ActivityContentDelegate(this,
    ContentConfig(R.layout.content_base))
  @IdRes open var contentContainerId = R.id.fragment_container

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      supportFragmentManager
        .beginTransaction()
        .replace(contentContainerId, createFragment(
          provideContent(),
          { putAll(intent?.extras ?: Bundle()) }
        ))
        .commit()
    }
  }

  abstract fun provideContent(): T
}
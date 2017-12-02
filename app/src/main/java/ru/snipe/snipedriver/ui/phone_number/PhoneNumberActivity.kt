package ru.snipe.snipedriver.ui.phone_number

import android.content.Context
import android.content.Intent
import ru.snipe.snipedriver.ui.base.BaseContentActivity
import ru.snipe.snipedriver.utils.createIntent

class PhoneNumberActivity : BaseContentActivity<PhoneNumberFragment>() {

  companion object {
    fun getIntent(context: Context): Intent {
      return createIntent(context, PhoneNumberActivity::class.java, {})
    }
  }

  override fun provideContent() = PhoneNumberFragment()
}
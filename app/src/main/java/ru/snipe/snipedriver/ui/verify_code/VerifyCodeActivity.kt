package ru.snipe.snipedriver.ui.verify_code

import android.content.Context
import android.content.Intent
import ru.snipe.snipedriver.ui.base.BaseContentActivity
import ru.snipe.snipedriver.utils.createIntent

class VerifyCodeActivity : BaseContentActivity<VerifyCodeFragment>() {
  companion object {
    fun getIntent(context: Context, phone: String): Intent {
      return createIntent(context, VerifyCodeActivity::class.java, {
        putExtra("phone", phone)
      })
    }
  }

  override fun provideContent() = VerifyCodeFragment()
}
package ru.snipe.snipedriver.ui.popup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.base.ActivityContentDelegate
import ru.snipe.snipedriver.ui.base.BaseActivity
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.asString

private const val EXTRA_MODE = "mode"

class PopupActivity : BaseActivity() {
  override var contentDelegate = ActivityContentDelegate(this,
    ContentConfig(R.layout.content_popup))

  companion object {
    fun getIntent(context: Context,
                  mode: Int): Intent {
      return Intent(context, PopupActivity::class.java).apply {
        putExtra(EXTRA_MODE, mode)
      }
    }
  }

  private val pic by bindView<ImageView>(R.id.iv_popup_pic)
  private val title by bindView<TextView>(R.id.tv_popup_title)
  private val posButton by bindView<TextView>(R.id.tv_popup_positive)
  private val negButton by bindView<TextView>(R.id.tv_popup_positive)

  override fun initView(view: Activity) {
    if (intent.extras.getInt(EXTRA_MODE) == 0) {
      title.text = "Вы прибыли на проспект Энгельса, Санкт-Петебург?"
      posButton.text = R.string.all_arrived.asString(this)
    } else {
      title.text = "Вы завершили доставку на проспект Энгельса, 107B?"
      posButton.text = R.string.all_finished.asString(this)
    }
    posButton.setOnClickListener { processButtonClick(it) }
    negButton.setOnClickListener { processButtonClick(it) }
  }

  private fun processButtonClick(v: View) {
    when (v.id) {
      R.id.tv_popup_positive -> setResult(Activity.RESULT_OK)
      R.id.tv_popup_negative -> setResult(Activity.RESULT_CANCELED)
    }
    ActivityCompat.finishAfterTransition(this)
  }
}
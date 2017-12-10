package ru.snipe.snipedriver.ui.popup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.TextView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.base.ActivityContentDelegate
import ru.snipe.snipedriver.ui.base.BaseActivity
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.setDebouncingOnClickListener

private const val EXTRA_MODE = "mode"

class CancelPopupActivity : BaseActivity() {
  override var contentDelegate = ActivityContentDelegate(this,
    ContentConfig(R.layout.content_popup_cancel))

  companion object {
    const val RESULT_CLICKED_BUTTON_NUMBER = "result"

    fun getIntent(context: Context,
                  mode: Int): Intent {
      return Intent(context, CancelPopupActivity::class.java).apply {
        putExtra(EXTRA_MODE, mode)
      }
    }
  }

  private val title by bindView<TextView>(R.id.tv_popup_cancel_title)
  private val posButton1 by bindView<TextView>(R.id.tv_popup_positive_1)
  private val posButton2 by bindView<TextView>(R.id.tv_popup_positive_2)
  private val posButton3 by bindView<TextView>(R.id.tv_popup_positive_3)
  private val divider2 by bindView<View>(R.id.tv_popup_positive_divider_2)
  private val divider3 by bindView<View>(R.id.tv_popup_positive_divider_3)
  private val negButton by bindView<TextView>(R.id.tv_popup_negative)

  override fun initView(view: Activity) {
    val mode = intent.extras.getInt(EXTRA_MODE)

    when (mode) {
      0 -> {
        title.text = "Ваш рейтинг принятия заказов не будет понижен. Оплата с клиента будет взята."
        posButton1.text = "Груз не соответствует фото"
        posButton2.text = "Нет связи с клиентом"
        posButton3.text = "Не брать оплату с клиента"
      }
      1 -> {
        title.text = "Вы находитесь на пути к месту разгрузки. Пожалуйста, расскажите что случилось:"
        posButton1.text = "Авария"
        posButton2.text = "Техническая неисправность"
        posButton3.visibility = View.GONE
        divider3.visibility = View.GONE
      }
      else -> {
        title.text = "Рейтинг принятия заказов будет понижен. Оплата с клиента не будет взята."
        posButton1.text = "Да, отменить"
        posButton2.visibility = View.GONE
        posButton3.visibility = View.GONE
        divider2.visibility = View.GONE
        divider3.visibility = View.GONE
      }
    }
    posButton1.setDebouncingOnClickListener { processButtonClick(it) }
    posButton2.setDebouncingOnClickListener { processButtonClick(it) }
    posButton3.setDebouncingOnClickListener { processButtonClick(it) }
    negButton.setDebouncingOnClickListener { processButtonClick(it) }
  }

  fun processButtonClick(v: View) {
    when (v.id) {
      R.id.tv_popup_positive_1 -> setResult(Activity.RESULT_OK, Intent().apply { putExtra(RESULT_CLICKED_BUTTON_NUMBER, 1) })
      R.id.tv_popup_positive_2 -> setResult(Activity.RESULT_OK, Intent().apply { putExtra(RESULT_CLICKED_BUTTON_NUMBER, 2) })
      R.id.tv_popup_positive_3 -> setResult(Activity.RESULT_OK, Intent().apply { putExtra(RESULT_CLICKED_BUTTON_NUMBER, 3) })
      R.id.tv_popup_negative -> setResult(Activity.RESULT_CANCELED)
    }
    ActivityCompat.finishAfterTransition(this)
  }
}
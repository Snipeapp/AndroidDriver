package ru.snipe.snipedriver.ui.popup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import ru.snipe.snipedriver.R

private const val EXTRA_MODE = "mode"

class PopupActivity : AppCompatActivity() {
  companion object {
    fun getIntent(context: Context,
                  mode: Int): Intent {
      return Intent(context, PopupActivity::class.java).apply {
        putExtra(EXTRA_MODE, mode)
      }
    }
  }

  @BindView(R.id.iv_popup_pic) lateinit var pic: ImageView
  @BindView(R.id.tv_popup_title) lateinit var title: TextView
  @BindView(R.id.tv_popup_positive) lateinit var posButton: TextView
  @BindView(R.id.tv_popup_negative) lateinit var negButton: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.content_popup)
    ButterKnife.bind(this)

    if (intent.extras.getInt(EXTRA_MODE) == 0) {
      title.text = "Вы прибыли на проспект Энгельса, Санкт-Петебург?"
      posButton.text = "Да, прибыл"
    } else {
      title.text = "Вы завершили доставку на проспект Энгельса, 107B?"
      posButton.text = "Да, завершил"
    }
  }

  @OnClick(R.id.tv_popup_positive, R.id.tv_popup_negative)
  fun onClick(v: View) {
    when (v.id) {
      R.id.tv_popup_positive -> setResult(Activity.RESULT_OK)
      R.id.tv_popup_negative -> setResult(Activity.RESULT_CANCELED)
    }
    ActivityCompat.finishAfterTransition(this)
  }
}
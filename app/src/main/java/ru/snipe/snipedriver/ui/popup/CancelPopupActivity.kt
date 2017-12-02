package ru.snipe.snipedriver.ui.popup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import ru.snipe.snipedriver.R

class CancelPopupActivity : AppCompatActivity() {
    @BindView(R.id.tv_popup_cancel_title) lateinit var title: TextView
    @BindView(R.id.tv_popup_positive_1) lateinit var posButton1: TextView
    @BindView(R.id.tv_popup_positive_2) lateinit var posButton2: TextView
    @BindView(R.id.tv_popup_positive_3) lateinit var posButton3: TextView
    @BindView(R.id.tv_popup_positive_divider_2) lateinit var divider2: View
    @BindView(R.id.tv_popup_positive_divider_3) lateinit var divider3: View
    @BindView(R.id.tv_popup_negative) lateinit var negButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_popup_cancel)
        ButterKnife.bind(this)

        val mode = intent.extras.getInt("mode")
        if (mode == 0) {
            title.text = "Ваш рейтинг принятия заказов не будет понижен. Оплата с клиента будет взята."
            posButton1.text = "Груз не соответствует фото"
            posButton2.text = "Нет связи с клиентом"
            posButton3.text = "Не брать оплату с клиента"
        } else if (mode == 1) {
            title.text = "Вы находитесь на пути к месту разгрузки. Пожалуйста, расскажите что случилось:"
            posButton1.text = "Авария"
            posButton2.text = "Техническая неисправность"
            posButton3.visibility = View.GONE
            divider3.visibility = View.GONE
        } else {
            title.text = "Рейтинг принятия заказов будет понижен. Оплата с клиента не будет взята."
            posButton1.text = "Да, отменить"
            posButton2.visibility = View.GONE
            posButton3.visibility = View.GONE
            divider2.visibility = View.GONE
            divider3.visibility = View.GONE
        }
    }

    @OnClick(R.id.tv_popup_positive_1, R.id.tv_popup_positive_2, R.id.tv_popup_positive_3, R.id.tv_popup_negative)
    fun onClick(v: View) {
        when (v.id) {
            R.id.tv_popup_positive_1 -> setResult(Activity.RESULT_OK, Intent().apply { putExtra("res", 1) })
            R.id.tv_popup_positive_2 -> setResult(Activity.RESULT_OK, Intent().apply { putExtra("res", 2) })
            R.id.tv_popup_positive_3 -> setResult(Activity.RESULT_OK, Intent().apply { putExtra("res", 3) })
            R.id.tv_popup_negative -> setResult(Activity.RESULT_CANCELED)
        }
        ActivityCompat.finishAfterTransition(this)
    }
}
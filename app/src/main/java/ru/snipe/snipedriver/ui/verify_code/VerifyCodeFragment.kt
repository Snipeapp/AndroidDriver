package ru.snipe.snipedriver.ui.verify_code

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.TextAppearanceSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.getAppComponent
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.ui.views.OptionsItem
import ru.snipe.snipedriver.ui.views.SimpleClickableSpan
import ru.snipe.snipedriver.ui.views.ToolbarCompat
import ru.snipe.snipedriver.utils.*

class VerifyCodeFragment : BaseMvpFragment<Unit>(), VerifyCodeView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_verify_code))

  companion object {
    const val EXTRA_PHONE = "phone"
  }

  private val toolbar by bindView<ToolbarCompat>(R.id.toolbar)
  private val codeInput by bindView<EditText>(R.id.verify_code_input_phone)
  private val description by bindView<TextView>(R.id.verify_code_txt_description)
  private val loadingLayout by bindView<View>(R.id.layout_verify_code_loading)

  private val phone by lazy { arguments!!.getString(EXTRA_PHONE, null) }

  @InjectPresenter
  lateinit var presenter: VerifyCodePresenter

  @ProvidePresenter
  fun providePresenter(): VerifyCodePresenter {
    return context!!.getAppComponent()
      .plusVerifyCodeComponent(VerifyCodeModule(phone))
      .presenter()
  }

  override fun initView(view: View) {
    toolbar.titleText = R.string.verify_code_title.asString(context)
    toolbar.iconClickAction = { activity?.hideKeyboard(); activity?.onBackPressed() }
    toolbar.optionsItem = OptionsItem(R.string.all_ready.asString(context), 0, { processReadyClick() })

    description.movementMethod = LinkMovementMethod()
    description.highlightColor = Color.TRANSPARENT
    description.text = buildDescriptionTitle()

    codeInput.setOnEditorActionListener({ _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_NEXT) {
        processReadyClick()
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    })
    codeSent()
  }

  private fun buildDescriptionTitle(): SpannableStringBuilder {
    return SpannableStringBuilder()
      //TODO: Отображать отформатированный телефон
      .append(R.string.verify_code_description.asString(context, phone))
      .appendSpace()
      .withSpans(R.string.verify_code_send_new.asString(context),
        SimpleClickableSpan({ presenter.onResendButtonClicked() }),
        TextAppearanceSpan(context, R.style.M24Black))
  }

  private fun processReadyClick() {
    activity?.hideKeyboard()
    presenter.onCodeValidated(codeInput.text.toString())
  }

  override fun showLoading() {
    loadingLayout.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    loadingLayout.visibility = View.GONE
  }

  override fun codeSent() {
    Toast.makeText(context, R.string.verify_code_sent_code.asString(context), Toast.LENGTH_SHORT).show()
    Handler().postDelayed({ activity!!.showKeyboard() }, 1000)
  }

  override fun codeVerified() {
    Toast.makeText(context, R.string.verify_code_correct_code.asString(context), Toast.LENGTH_SHORT).show()

    //TODO: Вынести логику сохранения в преференсы в презентер
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putBoolean("logged", true)
      .apply()

    val intent = FreeDriverActivity.getIntent(context!!)
      .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    ActivityCompat.startActivity(context!!, intent, null)
  }

  override fun showError(error: String) {
    Snackbar.make(toolbar, error, Snackbar.LENGTH_SHORT).show()
    Handler().postDelayed({ activity!!.showKeyboard() }, 1000)
  }
}
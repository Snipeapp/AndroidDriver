package ru.snipe.snipedriver.ui.verify_code

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.hideKeyboard
import ru.snipe.snipedriver.utils.showKeyboard
import ru.snipe.snipedriver.utils.withTint

class VerifyCodeFragment : BaseMvpFragment<Unit>(), VerifyCodeView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_verify_code))

  companion object {
    const val EXTRA_PHONE = "phone"
  }

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val codeInput by bindView<EditText>(R.id.edittext_verify_code)
  private val description by bindView<TextView>(R.id.tv_verify_code_description)
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
    setHasOptionsMenu(true)

    description.movementMethod = LinkMovementMethod()
    description.highlightColor = Color.TRANSPARENT
    description.text =
      SpannableStringBuilder().apply {
        append(getString(R.string.verify_code_description, phone))
        append(" ")
        append(SpannableString(getString(R.string.verify_code_send_new)).apply {
          val resendSpan = ResendSpan({ presenter.onResendButtonClicked() })
          setSpan(resendSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
          setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.colorAccent)),
            0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        })
      }

    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    toolbar.navigationIcon?.withTint(R.color.colorAccent)

    codeInput.setOnEditorActionListener({ _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_NEXT) {
        onCodeValid()
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    })
    codeSent()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
    inflater.inflate(R.menu.menu_ready, menu)

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_ready -> {
        onCodeValid()
      }
      android.R.id.home -> {
        activity!!.onBackPressed()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun onCodeValid() {
    activity!!.hideKeyboard()
    presenter.onCodeValidated(codeInput.text.toString())
  }

  override fun showLoading() {
    loadingLayout.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    loadingLayout.visibility = View.GONE
  }

  override fun codeSent() {
    Toast.makeText(context, "Код отправлен", Toast.LENGTH_SHORT).show()
    Handler().postDelayed({ activity!!.showKeyboard() }, 1000)
  }

  override fun codeVerified() {
    Toast.makeText(context, "Код верный", Toast.LENGTH_SHORT).show()

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

private class ResendSpan(val onButtonCLickedAction: (Boolean) -> Unit) : ClickableSpan() {
  override fun onClick(widget: View?) {
    onButtonCLickedAction.invoke(true)
  }

  override fun updateDrawState(ds: TextPaint?) {
    super.updateDrawState(ds)
    ds?.isUnderlineText = false
    ds?.isFakeBoldText = false
  }
}
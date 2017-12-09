package ru.snipe.snipedriver.ui.phone_number

import android.Manifest
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.tbruyelle.rxpermissions.RxPermissions
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.getAppComponent
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.verify_code.VerifyCodeActivity
import ru.snipe.snipedriver.ui.views.OptionsItem
import ru.snipe.snipedriver.ui.views.ToolbarCompat
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.asString
import ru.snipe.snipedriver.utils.hideKeyboard
import ru.snipe.snipedriver.utils.showKeyboard

class PhoneNumberFragment : BaseMvpFragment<Unit>(), PhoneNumberView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_phone_number))

  private val toolbar by bindView<ToolbarCompat>(R.id.toolbar)
  private val numberInput by bindView<EditText>(R.id.phone_number_input_phone)
  private val loadingLayout by bindView<View>(R.id.layout_phone_number_loading)

  @InjectPresenter
  lateinit var presenter: PhoneNumberPresenter

  @ProvidePresenter
  fun providePresenter(): PhoneNumberPresenter {
    return context!!.getAppComponent()
      .plusPhoneNumberComponent(PhoneNumberModule())
      .presenter()
  }

  override fun initView(view: View) {
    toolbar.iconClickAction = { activity?.hideKeyboard(); activity?.onBackPressed() }
    toolbar.optionsItem = OptionsItem(R.string.all_next.asString(context), 0, { tryGoNext() })
    numberInput.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    numberInput.setOnEditorActionListener({ _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_NEXT) {
        tryGoNext()
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    })
    RxPermissions(activity!!)
      .request(Manifest.permission.ACCESS_FINE_LOCATION)
      .subscribe({ granted ->
        if (!granted) {
          showError(R.string.phone_number_error_no_geo_permissions)
        }
      })
  }


  override fun onResume() {
    super.onResume()
    activity!!.showKeyboard()
  }

  private fun tryGoNext() {
    activity?.hideKeyboard()
    presenter.onNextClicked(numberInput.text.toString())
  }

  override fun showLoading() {
    loadingLayout.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    loadingLayout.visibility = View.GONE
  }

  override fun codeSent() {
    ActivityCompat.startActivity(context!!,
      VerifyCodeActivity.getIntent(context!!, numberInput.text.toString()),
      ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
  }

  override fun showError(@StringRes errorRes: Int) {
    Snackbar.make(toolbar, errorRes, Snackbar.LENGTH_SHORT).show()
  }
}
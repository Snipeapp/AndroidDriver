package ru.snipe.snipedriver.ui.onboarding

import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.widget.Button
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.ui.phone_number.PhoneNumberActivity
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.createIntent

class OnBoardingFragment : BaseMvpFragment<Unit>(), OnBoardingView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_onboarding))

  private val signUpButton by bindView<Button>(R.id.button_onboarding_sign_up)
  private val loginInButton by bindView<Button>(R.id.button_onboarding_log_in)

  @InjectPresenter
  internal lateinit var presenter: OnBoardingPresenter

  @ProvidePresenter
  fun providePresenter(): OnBoardingPresenter {
    throw UnsupportedOperationException("add provide presenter logic")
  }

  override fun initView(view: View) {
    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("logged", false)) {
      ActivityCompat.startActivity(context!!,
        createIntent(context!!, FreeDriverActivity::class.java, {}),
        null)
      activity!!.finish()
    }
    signUpButton.setOnClickListener { presenter.onSignUpButtonClicked() }
    loginInButton.setOnClickListener { presenter.onLoginButtonClicked() }
  }

  override fun switchToPhoneInsertScreen() {
    ActivityCompat.startActivity(context!!,
      createIntent(context!!, PhoneNumberActivity::class.java, {}),
      ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
  }

  override fun showErrorMessage(error: String) {
    Snackbar.make(signUpButton, error, Snackbar.LENGTH_SHORT).show()
  }
}
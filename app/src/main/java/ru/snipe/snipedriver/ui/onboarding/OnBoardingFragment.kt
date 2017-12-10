package ru.snipe.snipedriver.ui.onboarding

import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.getAppComponent
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.ui.phone_number.PhoneNumberActivity
import ru.snipe.snipedriver.ui.views.SimpleClickableSpan
import ru.snipe.snipedriver.utils.*

class OnBoardingFragment : BaseMvpFragment<Unit>(), OnBoardingView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_onboarding))

  private val signUpButton by bindView<Button>(R.id.onboarding_btn_signup)
  private val loginTitle by bindView<TextView>(R.id.onboarding_txt_login_title)

  @InjectPresenter
  internal lateinit var presenter: OnBoardingPresenter

  @ProvidePresenter
  fun providePresenter(): OnBoardingPresenter {
    return context!!.getAppComponent()
      .plusOnBoardingComponent(OnBoardingModule())
      .presenter()
  }

  override fun initView(view: View) {
    //TODO: вынести в презентер
    if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("logged", false)) {
      ActivityCompat.startActivity(context!!,
        createIntent(context!!, FreeDriverActivity::class.java, {}),
        null)
      activity!!.finish()
    }
    signUpButton.setDebouncingOnClickListener { presenter.onSignUpButtonClicked() }
    loginTitle.text = buildLoginTitle()
    loginTitle.movementMethod = LinkMovementMethod.getInstance()
  }

  private fun buildLoginTitle(): SpannableStringBuilder {
    return SpannableStringBuilder()
      .append(R.string.onboarding_login_title_question.asString(context))
      .appendSpace()
      .withSpans(R.string.onboarding_login_title_login_word.asString(context),
        SimpleClickableSpan({ presenter.onLoginButtonClicked() }),
        TextAppearanceSpan(context, R.style.M28Black))
  }

  override fun switchToPhoneInsertScreen() {
    ActivityCompat.startActivity(context!!,
      PhoneNumberActivity.getIntent(context!!),
      ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
  }

  override fun showErrorMessage(error: String) {
    Snackbar.make(signUpButton, error, Snackbar.LENGTH_SHORT).show()
  }
}
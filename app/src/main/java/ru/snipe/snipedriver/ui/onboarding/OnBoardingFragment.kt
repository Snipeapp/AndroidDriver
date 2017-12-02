package ru.snipe.snipedriver.ui.onboarding

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.subjects.PublishSubject
import ru.snipe.snipedriver.App
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.utils.createIntent
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.ui.phone_number.PhoneNumberActivity
import javax.inject.Inject

class OnBoardingFragment : Fragment(), OnBoardingView {
    @Inject lateinit var presenter: OnBoardingPresenter
    @BindView(R.id.button_onboarding_sign_up) lateinit var button: Button
    val clickSubject = PublishSubject.create<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity!!.application as App).component.inject(this)
        super.onCreate(savedInstanceState)

        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("logged", false)) {
            ActivityCompat.startActivity(context!!,
              createIntent(context!!, FreeDriverActivity::class.java, {}),
                    null)
            activity!!.finish()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.content_onboarding, container, false)
        ButterKnife.bind(this, view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    @OnClick(R.id.button_onboarding_sign_up, R.id.button_onboarding_log_in)
    fun onClick(v: View) {
        when (v.id) {
            R.id.button_onboarding_sign_up, R.id.button_onboarding_log_in -> {
                clickSubject.onNext(Object())
            }
        }
    }

    override fun success() {
        ActivityCompat.startActivity(context!!,
          createIntent(context!!, PhoneNumberActivity::class.java, {}),
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
    }

    override fun showError(error: String) {
        Snackbar.make(button, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun loginButtonClicked() = clickSubject
    override fun signUpButtonClicked() = clickSubject
}

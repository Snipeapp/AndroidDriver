package ru.snipe.snipedriver.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.hannesdorfmann.mosby3.mvi.MviFragment
import io.reactivex.Observable
import ru.snipe.snipedriver.DaggerAppComponent
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.presenter.OnBoardingPresenter
import javax.inject.Inject

class OnBoardingFragment : MviFragment<OnBoardingView, OnBoardingPresenter>(), OnBoardingView {
    @Inject lateinit var presenter: OnBoardingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.create().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_onboarding, container, false)
        ButterKnife.bind(this, view)

        return view
    }

    @OnClick(R.id.button_onboarding_sign_up, R.id.button_onboarding_log_in)
    fun onClick(v: View) {
        when (v.id) {
            R.id.button_onboarding_sign_up -> {

            }
            R.id.button_onboarding_log_in -> {

            }
        }
    }

    override fun loginButtonClicked(): Observable<Boolean> = Observable.never()
    override fun signUpButtonClicked(): Observable<Boolean> = Observable.never()
    override fun createPresenter() = presenter
}

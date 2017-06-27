package ru.snipe.snipedriver.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hannesdorfmann.mosby3.mvi.MviFragment
import io.reactivex.Observable
import ru.snipe.snipedriver.DaggerAppComponent
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.presenter.LoginPresenter
import ru.snipe.snipedriver.presenter.LoginViewState
import javax.inject.Inject

class LoginFragment : MviFragment<LoginView, LoginPresenter>(), LoginView {
    @BindView(R.id.email) lateinit var mEmailView: AutoCompleteTextView
    @BindView(R.id.password) lateinit var mPasswordView: EditText
    @BindView(R.id.login_progress) lateinit var mProgressView: View
    @BindView(R.id.login_form) lateinit var mLoginFormView: View

    @Inject lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.create().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_login, container, false)
        ButterKnife.bind(this, view)

        mPasswordView.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        return view
    }

    @OnClick(R.id.email_sign_in_button)
    fun onClick(v: View) {
        when (v.id) {
            R.id.email_sign_in_button -> {//attemptLogin()
            }
        }
    }

    override fun render(state: LoginViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginButtonClicked(): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createPresenter() = presenter
}

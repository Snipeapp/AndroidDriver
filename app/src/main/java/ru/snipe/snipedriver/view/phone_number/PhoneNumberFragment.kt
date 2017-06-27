package ru.snipe.snipedriver.view.phone_number

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.snipe.snipedriver.DaggerAppComponent
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.presenter.PhoneNumberPresenter
import ru.snipe.snipedriver.view.verify_code.VerifyCodeFragment
import javax.inject.Inject

class PhoneNumberFragment : Fragment(), PhoneNumberView {
    //MviFragment<PhoneNumberView, PhoneNumberPresenter>()
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.edittext_phone_number) lateinit var numberInput: EditText
    @BindView(R.id.layout_phone_number_loading) lateinit var loadingLayout: View

    @Inject lateinit var presenter: PhoneNumberPresenter

    val phoneSubject: PublishSubject<String> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.create().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        val view = inflater!!.inflate(R.layout.fragment_phone_number, container, false)
        ButterKnife.bind(this, view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        numberInput.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
            inflater.inflate(R.menu.menu_next, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_next -> phoneSubject.onNext(numberInput.text.toString())
            android.R.id.home -> activity.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun nextClicked(): Observable<String> {
        return phoneSubject
    }

    override fun codeSent() {
        val fragment = VerifyCodeFragment().apply {
            arguments = Bundle().apply {
                putString("phone", numberInput.text.toString())
            }
        }
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(fragment::class.java.canonicalName)
                .commit()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}

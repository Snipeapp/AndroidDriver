package ru.snipe.snipedriver.ui.phone_number

import android.Manifest
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions.RxPermissions
import io.reactivex.Observable
import ru.snipe.snipedriver.*
import ru.snipe.snipedriver.ui.verify_code.VerifyCodeActivity
import ru.snipe.snipedriver.utils.createIntent
import ru.snipe.snipedriver.utils.hideKeyboard
import ru.snipe.snipedriver.utils.showKeyboard
import javax.inject.Inject

class PhoneNumberFragment : Fragment(), PhoneNumberView {
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.edittext_phone_number) lateinit var numberInput: EditText
    @BindView(R.id.layout_phone_number_loading) lateinit var loadingLayout: View

    @Inject lateinit var presenter: PhoneNumberPresenter

    val phoneSubject: PublishRelay<String> = PublishRelay.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity!!.application as App).component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.content_phone_number, container, false)
        ButterKnife.bind(this, view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = getTintedDrawable(activity!!, R.drawable.ic_back, R.color.colorAccent)

        numberInput.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        numberInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                tryGoNext()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        });
        return view
    }

    fun getTintedDrawable(context: Context, @DrawableRes drawable: Int, @ColorRes color: Int): Drawable {
        val d = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawable)!!)
        DrawableCompat.setTint(d, ContextCompat.getColor(context, color))
        return d
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        RxPermissions(activity!!)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (!granted) {
                        showError("Для корректной работы приложению необходимо разрешить доступ к геопозиции")
                    }
                })
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        activity!!.showKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
            inflater.inflate(R.menu.menu_next, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_next -> {
                tryGoNext()
            }
            android.R.id.home -> {
                activity!!.hideKeyboard()
                activity!!.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryGoNext() {
        val phone = numberInput.text.toString()
        if (phone.replace("[^0-9]+".toRegex(), "").matches("[0-9]{11}|9[0-9]{9}".toRegex())) {
            activity!!.hideKeyboard()
            phoneSubject.accept(phone)
        } else {
            showError("Ошибка в номере телефона")
        }
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
        ActivityCompat.startActivity(context!!, createIntent(context!!, VerifyCodeActivity::class.java, {
            putExtra("phone", numberInput.text.toString())
        }), ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
    }

    override fun showError(error: String) {
        Snackbar.make(toolbar, error, Snackbar.LENGTH_SHORT).show()
    }
}

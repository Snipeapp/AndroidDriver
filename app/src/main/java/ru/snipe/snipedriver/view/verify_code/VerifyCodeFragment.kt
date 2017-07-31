package ru.snipe.snipedriver.view.verify_code

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v13.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.subjects.PublishSubject
import ru.snipe.snipedriver.*
import ru.snipe.snipedriver.presenter.VerifyCodePresenter
import ru.snipe.snipedriver.view.free_driver_mode.FreeDriverActivity
import javax.inject.Inject

class VerifyCodeFragment : Fragment(), VerifyCodeView { //MviFragment<VerifyCodeView, VerifyCodePresenter>()
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.edittext_verify_code) lateinit var codeInput: EditText
    @BindView(R.id.tv_verify_code_description) lateinit var description: TextView
    @BindView(R.id.layout_verify_code_loading) lateinit var loadingLayout: View

    @Inject lateinit var presenter: VerifyCodePresenter

    val resendSubject: PublishSubject<Boolean> = PublishSubject.create()
    val readySubject: PublishSubject<String> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity.application as App).component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        val phone = arguments.getString("phone", "")
        presenter.phone = phone

        val view = inflater!!.inflate(R.layout.fragment_verify_code, container, false)
        ButterKnife.bind(this, view)

        description.movementMethod = LinkMovementMethod()
        description.highlightColor = Color.TRANSPARENT
        description.text =
                SpannableStringBuilder().apply {
                    append(getString(R.string.verify_code_description, phone))
                    append(" ")
                    append(SpannableString(getString(R.string.verify_code_send_new)).apply {
                        setSpan(ResendSpan(this@VerifyCodeFragment), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
                                0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    })
                }

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = getTintedDrawable(activity, R.drawable.back, R.color.colorAccent)

        codeInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                tryGoNext()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        });


        codeSent()

        return view
    }

    fun getTintedDrawable(context: Context, @DrawableRes drawable: Int, @ColorRes color: Int): Drawable {
        val d = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawable))
        DrawableCompat.setTint(d, ContextCompat.getColor(context, color))
        return d
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
            inflater.inflate(R.menu.menu_ready, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_ready -> {
                tryGoNext()
            }
            android.R.id.home -> {
                activity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryGoNext() {
        activity.hideKeyboard()
        readySubject.onNext(codeInput.text.toString())
    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun codeSent() {
        Toast.makeText(context, "Код отправлен", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ activity.showKeyboard() }, 1000)
    }

    override fun codeVerified() {
        Toast.makeText(context, "Код верный", Toast.LENGTH_SHORT).show()
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("logged", true)
                .apply()

        ActivityCompat.startActivity(context,
                createIntent(context, FreeDriverActivity::class.java, {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }),
                null)
    }

    override fun showError(error: String) {
        Snackbar.make(toolbar, error, Snackbar.LENGTH_SHORT).show()
        Handler().postDelayed({ activity.showKeyboard() }, 1000)
    }

    override fun resendClicked() = resendSubject
    override fun readyClicked() = readySubject
//    override fun createPresenter() = presenter
}

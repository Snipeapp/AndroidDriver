package ru.snipe.snipedriver.view.verify_code

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvi.MviFragment
import ru.snipe.snipedriver.DaggerAppComponent
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.presenter.VerifyCodePresenter
import javax.inject.Inject

class VerifyCodeFragment : MviFragment<VerifyCodeView, VerifyCodePresenter>(), VerifyCodeView {
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.edittext_verify_code) lateinit var codeInput: EditText
    @BindView(R.id.layout_verify_code_loading) lateinit var loadingLayout: View

    @Inject lateinit var presenter: VerifyCodePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.create().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)

        val view = inflater!!.inflate(R.layout.fragment_verify_code, container, false)
        ButterKnife.bind(this, view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
            inflater.inflate(R.menu.menu_ready, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_ready -> {
                Toast.makeText(activity, "ready", Toast.LENGTH_SHORT).show()
            }
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

    override fun createPresenter() = presenter
}

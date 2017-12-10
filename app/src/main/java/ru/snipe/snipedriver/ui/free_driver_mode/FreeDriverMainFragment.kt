package ru.snipe.snipedriver.ui.free_driver_mode

import android.Manifest
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.tbruyelle.rxpermissions.RxPermissions
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.getAppComponent
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.driver_mode.DriverActivity
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.isVisible
import ru.snipe.snipedriver.utils.setDebouncingOnClickListener

class FreeDriverMainFragment : BaseMvpFragment<Unit>(), FreeDriverMainView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_main_free_driver))

  private val shadow by bindView<View>(R.id.view_free_driver_shadow)
  private val progressLayout by bindView<View>(R.id.layout_free_driver_loading)
  private val bottomNavigationView by bindView<BottomNavigationView>(R.id.bottom_nav_view_free_driver)

  private var toolbarButton by bindProperty<TextView>()

  @InjectPresenter
  internal lateinit var presenter: FreeDriverMainPresenter

  @ProvidePresenter
  fun providePresenter(): FreeDriverMainPresenter {
    return context!!.getAppComponent()
      .plusFreeDriverMainComponent(FreeDriverMainModule())
      .presenter()
  }

  override fun initView(view: View) {
    val toolbar = (activity as FreeDriverMainHolder).toolbar
    toolbar.isVisible = true
    toolbarButton = toolbar.findViewById(R.id.toolbar_button)
    toolbarButton.setDebouncingOnClickListener { presenter.statusClicked() }

    bottomNavigationView.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.action_main -> {
          switchToMap()
        }
        R.id.action_stats -> {
          switchToStats()
        }
      }
      true
    }
    switchToMap()
  }

  private fun switchToStats() {
    childFragmentManager.beginTransaction()
      .replace(R.id.fragment_container, FreeDriverStatsFragment())
      .commit()
  }

  private fun switchToMap() {
    RxPermissions(activity!!)
      .request(Manifest.permission.ACCESS_FINE_LOCATION)
      .subscribe({ granted ->
        if (granted) {
          val supportMapFragment = FreeDriverMapFragment()
          childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, supportMapFragment)
            .commit()
        }
      })
  }

  override fun setStatus(activated: Boolean) {
    toolbarButton.isActivated = activated
    if (activated) {
      shadow.visibility = View.GONE
      bottomNavigationView.visibility = View.GONE
    } else {
      shadow.visibility = View.VISIBLE
      bottomNavigationView.visibility = View.VISIBLE
    }
  }

  override fun showLoading() {
    progressLayout.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progressLayout.visibility = View.GONE
  }

  override fun driveRequest() {
    showBottomSheet()
  }

  override fun showError(error: String) {
    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
  }

  override fun goToDriverMode() {
    ActivityCompat.startActivity(activity!!, Intent(context, DriverActivity::class.java), null)
    activity!!.finish()
  }

  private fun showBottomSheet() {
    val bottomSheetDialog = BottomSheetDialog(context!!)
    bottomSheetDialog.setContentView(R.layout.layout_sheet_map)
    bottomSheetDialog.findViewById<LinearLayout>(R.id.layout_bottom_sheet_map)?.setDebouncingOnClickListener { presenter.requestAccepted() }
    bottomSheetDialog.show()
  }
}
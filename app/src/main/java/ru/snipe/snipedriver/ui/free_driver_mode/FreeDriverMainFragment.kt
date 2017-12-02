package ru.snipe.snipedriver.ui.free_driver_mode

import android.annotation.SuppressLint
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.getAppComponent
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.driver_mode.DriverActivity
import ru.snipe.snipedriver.utils.ContentConfig

interface FreeDriverMainHolder {
  fun switchToStats()
  fun switchToMap()
}

class FreeDriverMainFragment : BaseMvpFragment<Unit>(), OnMapReadyCallback, FreeDriverMainView {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_main_free_driver))

  private val shadow by bindView<View>(R.id.view_free_driver_shadow)
  private val progressLayout by bindView<View>(R.id.layout_free_driver_loading)
  private val status by bindView<View>(R.id.tv_free_driver_toolbar_status)
  private val bottomNavigationView by bindView<BottomNavigationView>(R.id.bottom_nav_view_free_driver)

  private var map by bindPropertyOpt<GoogleMap>()

  @InjectPresenter
  lateinit var presenter: FreeDriverMainPresenter

  @ProvidePresenter
  fun providePrenenter(): FreeDriverMainPresenter {
    return context!!.getAppComponent()
      .plusFreeDriverMainComponent(FreeDriverMainModule())
      .presenter()
  }

  override fun initView(view: View) {
    bottomNavigationView.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.action_main -> {
          goToMap()
        }
        R.id.action_stats -> {
          (activity as FreeDriverMainHolder).switchToStats()
        }
      }
      true
    }
    status.setOnClickListener { presenter.statusClicked() }
    goToMap()
  }

  private fun goToMap() {
    (activity as FreeDriverMainHolder).switchToMap()
  }

  override fun setStatus(activated: Boolean) {
    status.isActivated = activated
    if (activated) {
      shadow.visibility = View.GONE
      bottomNavigationView.visibility = View.GONE
    } else {
      shadow.visibility = View.VISIBLE
      bottomNavigationView.visibility = View.VISIBLE
    }
  }

  @SuppressLint("MissingPermission")
  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    map?.isMyLocationEnabled = true
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
    bottomSheetDialog.findViewById<LinearLayout>(R.id.layout_bottom_sheet_map)?.setOnClickListener { presenter.requestAccepted() }
    bottomSheetDialog.show()
  }
}
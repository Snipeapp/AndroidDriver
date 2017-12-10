package ru.snipe.snipedriver.ui.driver_mode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.tbruyelle.rxpermissions.RxPermissions
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.getAppComponent
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.ui.base_mvp.BaseMvpFragment
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.ui.popup.PopupActivity
import ru.snipe.snipedriver.ui.views.ToolbarCompat
import ru.snipe.snipedriver.utils.*

private const val REQUEST_A = 20
private const val REQUEST_B = 21
private const val REQUEST_C = 22
private const val REQUEST_D = 23
private const val REQUEST_E = 24

class DriverFragment : BaseMvpFragment<Unit>(), DriverView, OnMapReadyCallback {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_driver))

  private val toolbar by bindView<ToolbarCompat>(R.id.toolbar_driver)
  private val progressLayout by bindView<View>(R.id.progress_container)
  private val title by bindView<TextView>(R.id.tv_driver_bottom_sheet_title)
  private val bottomSheet by bindView<View>(R.id.layout_driver_bottom_sheet)
  private val toolbarRiding by bindView<View>(R.id.layout_driver_toolbar_riding)
  private val toolbarBeginDelivery by bindView<View>(R.id.layout_driver_toolbar_begin_delivery)
  private val toolbarDelivery by bindView<View>(R.id.layout_driver_toolbar_delivery)
  private val driverFab by bindView<View>(R.id.fab_driver)

  private var map by bindPropertyOpt<GoogleMap>()

  @InjectPresenter
  internal lateinit var presenter: DriverPresenter

  @ProvidePresenter
  fun providePresenter(): DriverPresenter {
    return context!!.getAppComponent()
      .plusDriverComponent(DriverModule())
      .presenter()
  }

  override fun initView(view: View) {
    toolbar.navigationType = NavigationIconType.None
    toolbar.optionItems = listOf(
      OptionsItem(R.string.driver_options_call.asString(context), 0, { _, _ ->
        Toast.makeText(context, R.string.driver_call_client.asString(context), Toast.LENGTH_SHORT).show()
      }),
      OptionsItem(R.string.driver_options_cancel_delivery.asString(context), 0, { _, _ ->
        //TODO: Вынести в презентер
        when (presenter.currentState) {
          0 -> startActivityForResult(
            PopupActivity.getIntent(context!!, 2),
            REQUEST_E,
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
          1 -> startActivityForResult(
            PopupActivity.getIntent(context!!, 0),
            REQUEST_C,
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
          2 -> startActivityForResult(
            PopupActivity.getIntent(context!!, 1),
            REQUEST_D,
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
        }
      })
    )

    //TODO: Заменить на другой фреймворк
    RxPermissions(activity!!)
      .request(Manifest.permission.ACCESS_FINE_LOCATION)
      .subscribe({ granted ->
        if (granted) {
          val supportMapFragment = SupportMapFragment()
          supportMapFragment.getMapAsync(this)
          childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, supportMapFragment)
            .commit()
        }
      })

    bottomSheet.setDebouncingOnClickListener { presenter.moveToNextState() }
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    map = googleMap
  }

  override fun askForArrive() {
    startActivityForResult(
      PopupActivity.getIntent(context!!, 0),
      REQUEST_A,
      ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
  }

  override fun askForDeliveryArrive() {
    startActivityForResult(
      PopupActivity.getIntent(context!!, 1),
      REQUEST_B,
      ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!).toBundle())
  }

  override fun goToRatingScreen() {
    val bottomSheetDialog = context.createBottomSheetThemed()
    bottomSheetDialog.setContentView(R.layout.layout_sheet_rating)

    val ratingBar = bottomSheetDialog.findViewById<RatingBar>(R.id.rating_bottom_sheet_rating)!!
    val btn = bottomSheetDialog.findViewById<Button>(R.id.btn_bottom_sheet_rating_ready)!!
    ratingBar.setOnRatingBarChangeListener { _, _, _ -> btn.isEnabled = true }
    btn.setDebouncingOnClickListener {
      bottomSheetDialog.dismiss()
      presenter.customerRated()
    }
    bottomSheetDialog.setCancelable(false)
    bottomSheetDialog.show()
  }

  override fun deliveryFinished() {
    ActivityCompat.startActivity(activity!!, FreeDriverActivity.getIntent(context!!), null)
    ActivityCompat.finishAfterTransition(activity!!)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_A && resultCode == Activity.RESULT_OK) {
      presenter.driverArrived()
    }
    if (requestCode == REQUEST_B && resultCode == Activity.RESULT_OK) {
      presenter.driverDeliveryArrived()
    }
    if (requestCode == REQUEST_C && resultCode == Activity.RESULT_OK) {
      presenter.driverBeginDeliveryCanceled()
    }
    if (requestCode == REQUEST_D) {
      presenter.driverDeliveryCanceled()
    }
    if (requestCode == REQUEST_E) {
      if (resultCode == Activity.RESULT_OK) {
        presenter.driverCanceled()
      }
    }
  }

  override fun goToBeginDeliveryMode() {
    toolbarRiding.visibility = View.GONE
    toolbarBeginDelivery.visibility = View.VISIBLE
    toolbarDelivery.visibility = View.GONE
    title.text = R.string.driver_start_delivery.asString(context)
  }

  override fun goToDeliveryMode() {
    toolbarRiding.visibility = View.GONE
    toolbarBeginDelivery.visibility = View.GONE
    toolbarDelivery.visibility = View.VISIBLE
    title.text = R.string.driver_finish_order.asString(context)
  }

  override fun showLoading() {
    progressLayout.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progressLayout.visibility = View.GONE
  }

  override fun showError(error: String) {
    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
  }
}
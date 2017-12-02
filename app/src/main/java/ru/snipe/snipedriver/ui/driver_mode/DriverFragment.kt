package ru.snipe.snipedriver.ui.driver_mode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.tbruyelle.rxpermissions.RxPermissions
import ru.snipe.snipedriver.App
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.free_driver_mode.FreeDriverActivity
import ru.snipe.snipedriver.ui.popup.CancelPopupActivity
import ru.snipe.snipedriver.ui.popup.PopupActivity
import javax.inject.Inject

class DriverFragment : Fragment(), DriverView, OnMapReadyCallback {
    @BindView(R.id.toolbar_driver) lateinit var toolbar: Toolbar
    @BindView(R.id.layout_driver_loading) lateinit var progressLayout: View
    @BindView(R.id.tv_driver_bottom_sheet_title) lateinit var title: TextView
    @BindView(R.id.layout_driver_bottom_sheet) lateinit var bottomSheet: View
    @BindView(R.id.layout_driver_toolbar_riding) lateinit var toolbarRiding: View
    @BindView(R.id.layout_driver_toolbar_begin_delivery) lateinit var toolbarBeginDelivery: View
    @BindView(R.id.layout_driver_toolbar_delivery) lateinit var toolbarDelivery: View
    @BindView(R.id.fab_driver) lateinit var driverFab: View

    @Inject lateinit var presenter: DriverPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_driver, container, false)
        ButterKnife.bind(this, view)

        (activity.application as App).component.inject(this)

        presenter.attachView(this)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (!granted) {
//                        Snackbar.make(toolbar, "Для корректной работы приложению необходимо разрешить доступ к геопозиции", Snackbar.LENGTH_SHORT).show()
                    } else {
                        val supportMapFragment = SupportMapFragment()
                        supportMapFragment.getMapAsync(this)
                        childFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, supportMapFragment)
                                .commit()
                    }
                })

        bottomSheet.setOnClickListener {
            presenter.moveToNextState()
        }

        setHasOptionsMenu(true)

        return view
    }

    private var mMap: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
    }

    override fun askForArrive() {
        startActivityForResult(
                Intent(activity, PopupActivity::class.java).apply { putExtra("mode", 0) },
                20,
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
    }

    override fun askForDeliveryArrive() {
        startActivityForResult(
                Intent(activity, PopupActivity::class.java).apply { putExtra("mode", 1) },
                21,
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
    }

    override fun goToRatingScreen() {
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.layout_sheet_rating)

        val ratingBar = bottomSheetDialog.findViewById(R.id.rating_bottom_sheet_rating) as RatingBar
        val btn = bottomSheetDialog.findViewById<Button>(R.id.btn_bottom_sheet_rating_ready)
        ratingBar.setOnRatingBarChangeListener { _, _, _ ->
            btn?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
            btn?.alpha = 1f
            btn?.isEnabled = true
        }
        btn?.setOnClickListener {
            bottomSheetDialog.dismiss()
            presenter.customerRated()
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }

    override fun deliveryFinished() {
        ActivityCompat.startActivity(activity, Intent(activity, FreeDriverActivity::class.java), null)
        ActivityCompat.finishAfterTransition(activity)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.driverArrived()
            }
        }
        if (requestCode == 21) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.driverDeliveryArrived()
            }
        }
        if (requestCode == 22) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.driverBeginDeliveryCanceled()
            }
        }
        if (requestCode == 23) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.driverDeliveryCanceled()
            }
        }
        if (requestCode == 24) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.driverCanceled()
            }
        }
    }

    override fun goToBeginDeliveryMode() {
        toolbarRiding.visibility = View.GONE
        toolbarBeginDelivery.visibility = View.VISIBLE
        toolbarDelivery.visibility = View.GONE
        title.text = "Начать доставку"
    }

    override fun goToDeliveryMode() {
        toolbarRiding.visibility = View.GONE
        toolbarBeginDelivery.visibility = View.GONE
        toolbarDelivery.visibility = View.VISIBLE
        title.text = "Завершить заказ"
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_driver, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_call -> {
                Toast.makeText(context, "Звонок клиенту", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_cancel_delivery -> {
                when (presenter.state) {
                    0 -> startActivityForResult(
                            Intent(activity, CancelPopupActivity::class.java).apply { putExtra("mode", 2) },
                            24,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
                    1 -> startActivityForResult(
                            Intent(activity, CancelPopupActivity::class.java).apply { putExtra("mode", 0) },
                            22,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
                    2 -> startActivityForResult(
                            Intent(activity, CancelPopupActivity::class.java).apply { putExtra("mode", 1) },
                            23,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
                }
                return true
            }
        }
        return false
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

    @OnClick(R.id.fab_driver)
    fun onClick(v: View) {
    }
}
package ru.snipe.snipedriver.view.free_driver_mode

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.tbruyelle.rxpermissions.RxPermissions
import ru.snipe.snipedriver.App
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.createIntent
import ru.snipe.snipedriver.presenter.FreeDriverPresenter
import ru.snipe.snipedriver.view.driver_mode.DriverActivity
import ru.snipe.snipedriver.view.onboarding.OnBoardingActivity
import javax.inject.Inject

class FreeDriverActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, FreeDriverView {
    private var mMap: GoogleMap? = null

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.drawer_layout) lateinit var drawer: DrawerLayout
    @BindView(R.id.nav_view) lateinit var navigationView: NavigationView
    @BindView(R.id.bottom_nav_view_free_driver) lateinit var bottomNavigationView: BottomNavigationView
    @BindView(R.id.view_free_driver_shadow) lateinit var shadow: View
    @BindView(R.id.layout_free_driver_loading) lateinit var progressLayout: View
    @BindView(R.id.tv_free_driver_toolbar_status) lateinit var status: View

    @Inject lateinit var presenter: FreeDriverPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_driver)
        (application as App).component.inject(this)

        ButterKnife.bind(this)
        presenter.attachView(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_main -> {
                    goToMap()
                }
                R.id.action_stats -> {
                    goToStats()
                }
            }
            true
        }

        status.setOnClickListener { presenter.statusClicked() }

        goToMap()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
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

    private fun goToStats() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StatsFragment())
                .commit()
    }

    private fun goToMap() {
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (!granted) {
//                        Snackbar.make(toolbar, "Для корректной работы приложению необходимо разрешить доступ к геопозиции", Snackbar.LENGTH_SHORT).show()
                    } else {
                        val supportMapFragment = MapFragment()
//                        supportMapFragment.getMapAsync(this)
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, supportMapFragment)
                                .commit()
                    }
                })
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_main -> {

            }
            R.id.nav_navigation -> {

            }
            R.id.nav_feedback -> {

            }
            R.id.nav_star -> {

            }
            R.id.nav_help -> {

            }
            R.id.nav_logout -> {
                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putBoolean("logged", false)
                        .apply()

                ActivityCompat.startActivity(this,
                        createIntent(this, OnBoardingActivity::class.java, {}),
                        null)
                finish()
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.isMyLocationEnabled = true
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
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun goToDriverMode() {
        ActivityCompat.startActivity(this, Intent(this, DriverActivity::class.java), null)
        finish()
    }

    private fun showBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.sheet_map)
        bottomSheetDialog.findViewById<LinearLayout>(R.id.layout_bottom_sheet_map)?.setOnClickListener { presenter.requestAccepted() }
        bottomSheetDialog.show()
    }
}

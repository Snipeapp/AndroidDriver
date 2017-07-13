package ru.snipe.snipedriver.view.driver_mode

import android.Manifest
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v13.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.createIntent
import ru.snipe.snipedriver.view.onboarding.OnBoardingActivity


class DriverActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//                .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        val bottomNavigationView = findViewById(R.id.bottom_nav_view) as BottomNavigationView
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

        val status = findViewById(R.id.tv_toolbar_status) as TextView
        status.setOnClickListener { status.isActivated = !status.isActivated }

        RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ granted ->
                    if (!granted) {
                        Snackbar.make(toolbar, "Для корректной работы приложению необходимо разрешить доступ к геопозиции", Snackbar.LENGTH_SHORT).show()
                    }
                })

        goToMap()
    }

    private fun goToStats() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StatsFragment())
                .commit()
    }

    private fun goToMap() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SupportMapFragment())
                .commit()
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
//            R.id.nav_stats -> {
//            }
//            R.id.nav_feedback -> {
//            }
//            R.id.nav_star -> {
//            }
//            R.id.nav_help -> {
//            }
//            R.id.nav_logout -> {
//            }
//        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}

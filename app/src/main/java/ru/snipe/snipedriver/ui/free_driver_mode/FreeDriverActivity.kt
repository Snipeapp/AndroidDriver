package ru.snipe.snipedriver.ui.free_driver_mode

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.base.ActivityContentDelegate
import ru.snipe.snipedriver.ui.base.BaseContentActivity
import ru.snipe.snipedriver.ui.onboarding.OnBoardingActivity
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.createIntent

interface FreeDriverMainHolder {
  val toolbar: Toolbar
}

class FreeDriverActivity : BaseContentActivity<FreeDriverMainFragment>(), FreeDriverMainHolder,
  NavigationView.OnNavigationItemSelectedListener {

  companion object {
    fun getIntent(context: Context): Intent {
      return Intent(context, FreeDriverActivity::class.java)
    }
  }

  override var contentDelegate = ActivityContentDelegate(this,
    ContentConfig(R.layout.content_free_driver))

  private val drawer by bindView<DrawerLayout>(R.id.drawer_layout)
  private val navigationView by bindView<NavigationView>(R.id.nav_view)
  override val toolbar by bindView<Toolbar>(R.id.toolbar)

  override fun provideContent() = FreeDriverMainFragment()

  override fun initView(view: Activity) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.all_open_menu, R.string.all_close_menu)
    drawer.addDrawerListener(toggle)
    toggle.syncState()
    navigationView.setNavigationItemSelectedListener(this)
  }

  override fun onBackPressed() {
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
        //TODO: Вынести в презентер
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
}
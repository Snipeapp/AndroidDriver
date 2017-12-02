package ru.snipe.snipedriver.ui.free_driver_mode

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.base.BaseFragment
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.utils.ContentConfig
import ru.snipe.snipedriver.utils.isVisible
import java.util.*

class FreeDriverStatsFragment : BaseFragment() {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_stats))

  private val tabLayout by bindView<TabLayout>(R.id.tab_layout_stats)
  private val viewPager by bindView<ViewPager>(R.id.view_pager_stats)

  override fun initView(view: View) {
    (activity as FreeDriverMainHolder).toolbar.isVisible = false
    setupViewPager(viewPager)
    tabLayout.setupWithViewPager(viewPager)
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = Adapter(childFragmentManager)
    adapter.addFragment(FreeDriverStatsItemFragment(), "Дневная")
    adapter.addFragment(FreeDriverStatsItemFragment(), "Недельная")
    viewPager.adapter = adapter
  }
}

private class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
  private val fragments = ArrayList<Fragment>()
  private val fragmentTitles = ArrayList<String>()

  fun addFragment(fragment: Fragment, title: String) {
    fragments.add(fragment)
    fragmentTitles.add(title)
  }

  override fun getItem(position: Int): Fragment = fragments[position]
  override fun getCount(): Int = fragments.size
  override fun getPageTitle(position: Int): CharSequence = fragmentTitles[position]
}

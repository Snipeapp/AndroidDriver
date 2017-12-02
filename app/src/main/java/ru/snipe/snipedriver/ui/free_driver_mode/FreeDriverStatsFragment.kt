package ru.snipe.snipedriver.ui.free_driver_mode

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import ru.snipe.snipedriver.R
import java.util.*

class FreeDriverStatsFragment : Fragment() {
  @BindView(R.id.tab_layout_stats) lateinit var tabLayout: TabLayout
  @BindView(R.id.view_pager_stats) lateinit var viewPager: ViewPager

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.content_stats, container, false)
    ButterKnife.bind(this, view)
    setupViewPager(viewPager)
    tabLayout.setupWithViewPager(viewPager)
    return view
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = Adapter(childFragmentManager)
    adapter.addFragment(FreeDriverStatsItemFragment(), "Дневная")
    adapter.addFragment(FreeDriverStatsItemFragment(), "Недельная")
    viewPager.adapter = adapter
  }

  inner class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
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
}

package ru.snipe.snipedriver.ui.free_driver_mode

import ru.snipe.snipedriver.R
import ru.snipe.snipedriver.ui.base.BaseFragment
import ru.snipe.snipedriver.ui.base.FragmentContentDelegate
import ru.snipe.snipedriver.utils.ContentConfig

class FreeDriverStatsItemFragment : BaseFragment() {
  override val contentDelegate = FragmentContentDelegate(this,
    ContentConfig(R.layout.content_stats_inner))
}
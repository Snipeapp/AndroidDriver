package ru.snipe.snipedriver.ui.driver_mode

import ru.snipe.snipedriver.ui.base.BaseContentActivity

class DriverActivity : BaseContentActivity<DriverFragment>() {
  override fun provideContent() = DriverFragment()
}
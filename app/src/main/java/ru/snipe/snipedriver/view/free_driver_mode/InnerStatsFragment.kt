package ru.snipe.snipedriver.view.free_driver_mode

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import ru.snipe.snipedriver.R

class InnerStatsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats_inner, container, false)
        ButterKnife.bind(this, view)
        return view
    }
}
package com.example.myapplication.managers

import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.debugToaster.Toaster
import com.google.android.material.tabs.TabLayout

/**
 * Class responsible for handling the login of showing and hiding
 * notifications badges on the tabs
 */
class TabNotificationsHandler(private val tabLayout: TabLayout) {

    fun updateBadges() {
        for (i in 0..4) {
            tabLayout.getTabAt(i)?.customView?.let {
                it.findViewById<TextView>(R.id.notifications_text).text = getNrOfLogs(i).toString()
            }
        }
    }

    private fun getNrOfLogs(position: Int) = when (position) {
            0 -> Toaster.instance?.infoHolder?.logList?.size
            1 -> Toaster.instance?.infoHolder?.getNrOfLogsByType(LogType.Error)
            2 -> Toaster.instance?.infoHolder?.getNrOfLogsByType(LogType.Warning)
            3 -> Toaster.instance?.infoHolder?.getNrOfLogsByType(LogType.Debug)
            4 -> Toaster.instance?.infoHolder?.getNrOfLogsByType(LogType.Success)
            else -> 0
        }
}
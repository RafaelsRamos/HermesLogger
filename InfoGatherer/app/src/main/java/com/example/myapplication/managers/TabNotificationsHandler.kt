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

    private val infoHolder get() = Toaster.instance.infoHolder

    fun updateBadges() {
        for (i in 0..4) {
            tabLayout.getTabAt(i)?.customView?.let {
                it.findViewById<TextView>(R.id.notifications_text).text = getNrOfLogs(i).toString()
            }
        }
    }

    private fun getNrOfLogs(position: Int) = when (position) {
            0 -> infoHolder.logList.size
            1 -> infoHolder.getNrOfLogsByType(LogType.Error)
            2 -> infoHolder.getNrOfLogsByType(LogType.Warning)
            3 -> infoHolder.getNrOfLogsByType(LogType.Debug)
            4 -> infoHolder.getNrOfLogsByType(LogType.Success)
            else -> 0
        }
}
package com.spartancookie.hermeslogger.managers

import android.widget.TextView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.debugToaster.Toaster
import com.google.android.material.tabs.TabLayout

private const val MAX_NUMBER_OF_LOGS_FOR_DISPLAY = 999

/**
 * Class responsible for handling the login of showing and hiding
 * notifications badges on the tabs
 */
internal class TabNotificationsHandler(private val tabLayout: TabLayout) {

    private val infoHolder get() = Toaster.instance.infoHolder

    fun updateBadges() {
        for (i in 0..5) {
            getBadgeTextView(i)?.run {
                val nrOfLogs = getNrOfLogs(i)
                text = if (nrOfLogs > MAX_NUMBER_OF_LOGS_FOR_DISPLAY) {
                    "$MAX_NUMBER_OF_LOGS_FOR_DISPLAY+"
                } else {
                    getNrOfLogs(i).toString()
                }
            }
        }
    }

    private fun getBadgeTextView(tabIndex: Int): TextView? {
        val tab = tabLayout.getTabAt(tabIndex)
        return tab?.customView?.findViewById(R.id.notifications_text)
    }

    private fun getNrOfLogs(position: Int): Int = when (position) {
            0 -> infoHolder.logList.size
            1 -> infoHolder.getNrOfLogsByType(LogType.Info)
            2 -> infoHolder.getNrOfLogsByType(LogType.Success)
            3 -> infoHolder.getNrOfLogsByType(LogType.Debug)
            4 -> infoHolder.getNrOfLogsByType(LogType.Warning)
            5 -> infoHolder.getNrOfLogsByType(LogType.Error)
            else -> 0
        }
}
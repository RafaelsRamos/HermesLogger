package com.spartancookie.hermeslogger.managers

import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.core.EventType

/**
 * Class responsible for handling the login of showing and hiding
 * notifications badges on the tabs
 */
internal class TabNotificationsHandler(private val tabLayout: TabLayout) {

    private val infoHolder get() = HermesHandler.infoHolder

    fun updateBadges() {
        for (i in 0..5) {
            val nrOfLogs = getNrOfLogs(i)
            val nrOfLogsString = if (nrOfLogs > MAX_LOG_NR_FOR_DISPLAY) {
                "$MAX_LOG_NR_FOR_DISPLAY+"
            } else {
                getNrOfLogs(i).toString()
            }
            getBadgeTextView(i)?.text = nrOfLogsString
        }
    }

    private fun getBadgeTextView(tabIndex: Int): TextView? {
        val tab = tabLayout.getTabAt(tabIndex)
        return tab?.customView?.findViewById(R.id.notifications_text)
    }

    private fun getNrOfLogs(position: Int): Int = when (position) {
            0 -> infoHolder.eventList.size
            1 -> infoHolder.getNumberOfLogsByType(EventType.Success)
            2 -> infoHolder.getNumberOfLogsByType(EventType.Info)
            3 -> infoHolder.getNumberOfLogsByType(EventType.Debug)
            4 -> infoHolder.getNumberOfLogsByType(EventType.Warning)
            5 -> infoHolder.getNumberOfLogsByType(EventType.Error)
            else -> 0
        }

    companion object {

        private const val MAX_LOG_NR_FOR_DISPLAY = 999

    }
}
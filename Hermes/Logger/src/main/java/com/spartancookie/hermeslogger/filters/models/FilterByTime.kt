package com.spartancookie.hermeslogger.filters.models

import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.models.EventDataHolder
import java.util.*

internal class FilterByTime(
    private val fromMillis: Long,
    private val toMillis: Long = 0,
    override val name: String = buildString {
        append("from ${fromMillis / 1000}s back")
        if (toMillis > 0) {
            append(" to ${toMillis / 1000}s back")
        }
    },
) : Filter {

    init {
        require(fromMillis >= toMillis)
    }

    private val currentTimestamp get() = Date().time

    override fun filter(unfilteredList: List<EventDataHolder>) = unfilteredList.filter { it.isInTimeRange()  }

    private fun EventDataHolder.isInTimeRange(): Boolean {
        val lowerTimestamp = currentTimestamp - fromMillis
        val higherTimestamp = currentTimestamp - toMillis
        return creationDate.time in (lowerTimestamp - 1)..(higherTimestamp + 1)
    }
}
package com.spartancookie.hermeslogger.filters

import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.models.FilterByTag
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.models.EventDataHolder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class FilterByTagTests {

    @Test
    fun `test tag and type filters`() {
        val filters = listOf(
            FilterByType(EventType.Warning),
            FilterByType(EventType.Success),
            FilterByTag("msg")
        )

        FilterManager.clearFilters()
        FilterManager.addFilters(*filters.toTypedArray())

        val events = mutableListOf<EventDataHolder>().apply {
            addAll(createEventDataHolder(2, EventType.Error, "Random message 1"))
            addAll(createEventDataHolder(1, EventType.Success, "My msg"))
            addAll(createEventDataHolder(2, EventType.Debug))
            addAll(createEventDataHolder(2, EventType.Error, "Random message 4"))
            addAll(createEventDataHolder(5, EventType.Warning))
        }

        val filteredEvents = FilterManager.applyFilters(events)
        filteredEvents.assertTypes(expectedNrSuccess = 1)

        assert(filteredEvents.any { it.message.contains("msg") })
        assert(filteredEvents.none { !it.message.contains("msg") })
    }

}
package com.spartancookie.hermeslogger.filters

import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.core.models.EventDataHolder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class FilterByTypeTests {

    @Test
    fun `test filter by type 1`() {
        val filters = listOf(FilterByType(EventType.Debug), FilterByType(EventType.Success))

        FilterManager.clearFilters()
        FilterManager.addFilters(*filters.toTypedArray())

        val events = mutableListOf<EventDataHolder>().apply {
            addAll(createEventDataHolder(3, EventType.Error))
            addAll(createEventDataHolder(1, EventType.Success))
            addAll(createEventDataHolder(2, EventType.Debug))
            addAll(createEventDataHolder(5, EventType.Warning))
        }

        FilterManager.applyFilters(events).assertTypes(
            expectedNrSuccess = 1,
            expectedNrDebug = 2
        )
    }

    @Test
    fun `test filter by type 2`() {
        val filters = listOf(
            FilterByType(EventType.Warning),
            FilterByType(EventType.Success),
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

        FilterManager.applyFilters(events).assertTypes(
            expectedNrSuccess = 1,
            expectedNrWarning = 5
        )
    }
}
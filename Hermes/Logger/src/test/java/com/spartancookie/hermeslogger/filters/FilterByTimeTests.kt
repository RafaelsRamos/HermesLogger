package com.spartancookie.hermeslogger.filters

import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.models.FilterByTime
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.models.EventDataHolder
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class FilterByTimeTests {

    @Test
    fun `test type and time filters 1`() {
        val filters = listOf(
            FilterByType(EventType.Success),
            FilterByTime(10000, 0)
        )

        FilterManager.clearFilters()
        FilterManager.addFilters(*filters.toTypedArray())

        val event = createMockedDataHolder(EventType.Success, "My random message", Date(Date().time - 7000L))
        val events = mutableListOf(event)

        val filteredEvents = FilterManager.applyFilters(events)
        filteredEvents.assertTypes(expectedNrSuccess = 1)
    }

    @Test
    fun `test type and time filters 2`() {
        val filters = listOf(
            FilterByType(EventType.Success),
            FilterByType(EventType.Error),
            FilterByTime(8000, 3000)
        )

        FilterManager.clearFilters()
        FilterManager.addFilters(*filters.toTypedArray())

        val events = mutableListOf<EventDataHolder>().apply {
            add(createMockedDataHolder(EventType.Success, "My random message", Date(Date().time - 5000L)))
            add(createMockedDataHolder(EventType.Error, "My random message", Date(Date().time - 2000L)))
        }

        val filteredEvents = FilterManager.applyFilters(events)
        filteredEvents.assertTypes(expectedNrSuccess = 1)
    }

    @Test
    fun `test type and time filters 3`() {
        val filters = listOf(
            FilterByType(EventType.Success),
            FilterByType(EventType.Error),
            FilterByTime(18000, 2000)
        )

        FilterManager.clearFilters()
        FilterManager.addFilters(*filters.toTypedArray())

        val events = mutableListOf<EventDataHolder>().apply {
            add(createMockedDataHolder(EventType.Success, "My random message", Date(Date().time - 30000L)))
            add(createMockedDataHolder(EventType.Success, "My random message", Date(Date().time - 15000L)))
            add(createMockedDataHolder(EventType.Success, "My random message", Date(Date().time - 10000L)))
            add(createMockedDataHolder(EventType.Success, "My random message", Date(Date().time)))
            add(createMockedDataHolder(EventType.Error, "My random message", Date(Date().time - 2000L)))
        }

        val filteredEvents = FilterManager.applyFilters(events)
        filteredEvents.assertTypes(expectedNrSuccess = 2, expectedNrError = 1)
    }

    @Test
    fun `test time filter error input`() {
        assertThrows<IllegalArgumentException> {
            FilterByTime(10000, 10001)
        }
    }

}
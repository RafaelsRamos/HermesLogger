package com.spartancookie.hermeslogger.filters.sharedpreferences

import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.models.FilterByTag
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.utils.toFilters
import com.spartancookie.hermeslogger.utils.toStringSet
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class FilterSharedPreferencesTests {

    private val typeFilters = listOf(FilterByType(EventType.Warning), FilterByType(EventType.Success))
    private val tagFilters = listOf(FilterByTag("Message"), FilterByTag("Service"))

    @Test
    fun `test FilterByType conversion to and from json`() {
        val filtersStringSet = typeFilters.toStringSet()
        val filters = filtersStringSet.toFilters<FilterByType>()
        assert(filters.size == typeFilters.size)
        assert(filters == typeFilters)
    }

    @Test
    fun `test FilterByTag conversion to and from json`() {
        val filtersStringSet = tagFilters.toStringSet()
        val filters = filtersStringSet.toFilters<FilterByTag>()

        assert(filters.size == tagFilters.size)
        assert(filters == tagFilters)
    }
}
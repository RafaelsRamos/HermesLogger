package com.spartancookie.hermeslogger.filters

import com.spartancookie.hermeslogger.core.models.EventDataHolder

internal class FilterSet<T: Filter> {

    private val _filters = mutableListOf<T>()
    val filters get() = _filters.toList()

    /**
     * Filter the [events] into a List that results from the current filters.
     */
    fun filterResults(events: List<EventDataHolder>) = filters.filter(events)

    /**
     * Override [_filters] with the given [filters].
     */
    fun overrideFilters(filters: Collection<T>) {
        clear()
        _filters.addAll(filters)
    }

    /**
     * Check if the current filters contains [filter].
     */
    fun contains(filter: T) : Boolean = filters.contains(filter)

    /**
     * Add [filter] to the current filters, if it doesn't exist already.
     */
    fun addFilter(filter: T) {
        if (!filters.contains(filter)) {
            _filters.add(filter)
        }
    }

    /**
     * Remove [filter] if it already exists in [_filters].
     */
    fun removeFilter(filter: T) {
        if (filters.contains(filter)) {
            _filters.remove(filter)
        }
    }

    /**
     * Clear all current filters.
     */
    fun clear() {
        _filters.clear()
    }

    private fun List<Filter>.filter(events: List<EventDataHolder>): List<EventDataHolder> {

        if (isEmpty()) {
            return events
        }

        // Filter 1st layer (by Type)
        val eventsFilteredByType = mutableListOf<EventDataHolder>()

        return eventsFilteredByType.apply {
            this@filter.forEach {
                addAll(it.filter(events))
            }
        }
    }

}
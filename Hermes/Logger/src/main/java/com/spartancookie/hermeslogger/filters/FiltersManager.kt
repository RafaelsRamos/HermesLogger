package com.spartancookie.hermeslogger.filters

import android.content.Context
import com.spartancookie.hermeslogger.filters.models.FilterByTag
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.preferences.loadFilters
import com.spartancookie.hermeslogger.preferences.saveFilters
import java.lang.IllegalArgumentException

internal object FilterManager {

    private val typeFilterSet = FilterSet<FilterByType>()
    private val tagFilterSet = FilterSet<FilterByTag>()

    val typeFilters get() = typeFilterSet.filters
    val tagFilters get() = tagFilterSet.filters

    private val allFilters get(): List<Filter> = listOf(*typeFilters.toTypedArray(), *tagFilters.toTypedArray())

    /**
     * Load the filters saved locally, from the previous use.
     */
    fun initialize(ctx: Context) {
        val savedFilters = ctx.loadFilters()
        typeFilterSet.overrideFilters(savedFilters.filterIsInstance<FilterByType>())
        tagFilterSet.overrideFilters(savedFilters.filterIsInstance<FilterByTag>())
    }

    /**
     * Add [filters] into the current list of active filters, if not added already.
     * Additionally, save them into shared preferences.
     */
    fun addFilters(vararg filters: Filter) {
        filters.forEach { filter ->
            when(filter) {
                is FilterByType -> typeFilterSet.addFilter(filter)
                is FilterByTag -> tagFilterSet.addFilter(filter)
                else -> throw IllegalArgumentException("Invalid filter type")
            }
        }
    }

    /**
     * Save [allFilters] into [android.content.SharedPreferences]
     */
    fun saveFilters(ctx: Context) {
        ctx.saveFilters(allFilters)
    }

    /**
     * Remove [filters] from the current list of active filters.
     * Additionally, save the remaining filters into shared preferences.
     */
    fun removeFilters(vararg filters: Filter) {
        filters.forEach { filter ->
            when(filter) {
                is FilterByType -> typeFilterSet.removeFilter(filter)
                is FilterByTag -> tagFilterSet.removeFilter(filter)
                else -> throw IllegalArgumentException("Invalid filter type")
            }
        }
    }

    /**
     * Clear all filters
     */
    fun clearFilters() {
        typeFilterSet.clear()
        tagFilterSet.clear()
    }

    /**
     * Check if the current active filter list, already contains [filter].
     */
    fun contains(filter: Filter): Boolean {
        return when(filter) {
            is FilterByType -> typeFilterSet.contains(filter)
            is FilterByTag -> tagFilterSet.contains(filter)
            else -> throw IllegalArgumentException("Invalid filter type")
        }
    }

    /**
     * Filter [events] into a List that events, filtered by the current active filters.
     */
    fun applyFilters(events: List<EventDataHolder>): List<EventDataHolder> {
        val eventsFilteredByType = typeFilterSet.filterResults(events)
        return tagFilterSet.filterResults(eventsFilteredByType)
    }
}

/**
 * Apply event filters to the @receiver
 */
internal fun List<EventDataHolder>.applyFilters() = FilterManager.applyFilters(this)

internal fun EventDataHolder.applyFilters() = listOf(this).applyFilters()
package com.spartancookie.hermeslogger.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.filters.models.FilterByTag
import com.spartancookie.hermeslogger.filters.models.FilterByType
import com.spartancookie.hermeslogger.preferences.SharedPreferencesConstants.FiltersFileName
import com.spartancookie.hermeslogger.preferences.SharedPreferencesConstants.StoredFiltersByTagKey
import com.spartancookie.hermeslogger.preferences.SharedPreferencesConstants.StoredFiltersByTypeKey

/**
 * Load filters from SharedPreferences, on the file [FiltersFileName].
 */
internal fun Context.loadFilters(): List<Filter> {
    return mutableListOf<Filter>().apply {
        // Fetch stored Filters by type
        addAll(fetchFiltersToSharedPrefs(StoredFiltersByTypeKey).toFilters<FilterByType>())
        // Fetch stored Filters by tag
        addAll(fetchFiltersToSharedPrefs(StoredFiltersByTagKey).toFilters<FilterByTag>())
    }.toList()
}

/**
 * Save current filters on SharedPreferences, on the file [FiltersFileName].
 */
internal fun Context.saveFilters(filters: List<Filter>) {
    getSharedPreferences(FiltersFileName, MODE_PRIVATE)
        .edit()
        .apply {
            putStringSet(StoredFiltersByTypeKey, filters.filterIsInstance<FilterByType>().toStringSet())
            putStringSet(StoredFiltersByTagKey, filters.filterIsInstance<FilterByTag>().toStringSet())
        }
        .apply()
}

private fun Context.fetchFiltersToSharedPrefs(type: String): Set<String> {
    val prefs = getSharedPreferences(FiltersFileName, MODE_PRIVATE)
    return prefs.getStringSet(type, setOf()) ?: setOf()
}
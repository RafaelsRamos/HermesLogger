package com.spartancookie.hermeslogger.preferences

import com.google.gson.Gson
import com.spartancookie.hermeslogger.filters.Filter

/**
 * Convert the @receiver into a list of [T], where [T] is a subtype of [Filter].
 */
internal inline fun <reified T: Filter> Set<String>.toFilters(): List<T>{
    return map { Gson().fromJson(it, T::class.java) }
}

/**
 * Convert the @receiver into a Set of Strings, by converting the @receiver using Gson.
 */
internal fun List<Filter>.toStringSet(): Set<String> = map { Gson().toJson(it) }.toSet()
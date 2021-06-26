package com.rafaelsramos.hermes.models

import com.rafaelsramos.hermes.debugToaster.LogType
import com.rafaelsramos.hermes.ui.search.CustomSearch
import com.rafaelsramos.hermes.ui.search.contains
import java.util.*

internal data class LogDataHolder(val message: String, val duration: Long, val type: LogType, var extraInfo: String? = null, var genericInfo: String? = null) {

    val creationDate: Date = Calendar.getInstance().time
    var id: String = ""

}

/**
 * Filter the logs on [this] according to [CustomSearch]
 */
internal fun List<LogDataHolder>.filterLogs(cs: CustomSearch) = filter { dataHolder -> dataHolder.contains(cs) }
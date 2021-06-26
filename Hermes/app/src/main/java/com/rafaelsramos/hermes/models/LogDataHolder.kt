package com.example.myapplication.models

import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.ui.search.CustomSearch
import com.example.myapplication.ui.search.contains
import java.util.*

internal data class LogDataHolder(val message: String, val duration: Long, val type: LogType, var extraInfo: String? = null, var genericInfo: String? = null) {

    val creationDate: Date = Calendar.getInstance().time
    var id: String = ""

}

/**
 * Filter the logs on [this] according to [CustomSearch]
 */
internal fun List<LogDataHolder>.filterLogs(cs: CustomSearch) = filter { dataHolder -> dataHolder.contains(cs) }
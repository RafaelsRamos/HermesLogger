package com.spartancookie.hermeslogger.models

import android.os.Parcelable
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.ui.search.contains
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
internal data class EventDataHolder(
    val type: EventType,
    val message: String,
    val extraInfo: String? = null,
    val genericInfo: String? = null,
    val dataType: DataType? = null,
    val throwable: Throwable? = null,
    val tags: MutableList<String> = mutableListOf()
): Parcelable {

    val creationDate: Date = Calendar.getInstance().time
    var id: String = ""

}

/**
 * Filter the logs on [this] according to [CustomSearch]
 */
internal fun List<EventDataHolder>.filterLogs(cs: CustomSearch) = filter { dataHolder -> dataHolder.contains(cs) }

/**
 * Get log number, in the type the log is in.
 * @return Instance of [EventDataHolder]
 */
internal fun EventDataHolder.getLogTypeNumber(): String = id.filter { it.isDigit() }
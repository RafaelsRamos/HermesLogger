package com.spartancookie.hermeslogger.models

import android.os.Parcelable
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.ui.search.contains
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
internal data class LogDataHolder(val message: String, val duration: Long, val type: LogType, var extraInfo: String? = null, var genericInfo: String? = null, val dataType: DataType? = null): Parcelable {

    val creationDate: Date = Calendar.getInstance().time
    var id: String = ""

}

/**
 * Filter the logs on [this] according to [CustomSearch]
 */
internal fun List<LogDataHolder>.filterLogs(cs: CustomSearch) = filter { dataHolder -> dataHolder.contains(cs) }
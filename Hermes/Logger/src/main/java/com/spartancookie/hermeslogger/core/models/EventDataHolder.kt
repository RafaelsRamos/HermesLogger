package com.spartancookie.hermeslogger.core.models

import android.os.Parcelable
import com.spartancookie.formatter.DataType
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.ui.search.contains
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * # Data class that contains the information of an event.
 */
@Parcelize
internal data class EventDataHolder(

    /**
     * ## Type of event.
     */
    val type: EventType,

    /**
     * ## Event message.
     *
     * Should be concise, so that it can be used to identify the event context.
     */
    val message: String,

    /**
     * ## (Optional) Event description.
     *
     * Contains the bulk of the information regarding the event.
     */
    val description: String? = null,

    /**
     * ## (Optional) Generic system information.
     *
     * Snapshot of the system, fetched by calling [com.spartancookie.hermeslogger.core.SystemInfoBuildable.buildSystemSnapshotInfo]
     */
    val genericInfo: String? = null,

    /**
     * ## (Optional) Type of data being passed on [description]
     *
     * Informs the library regarding the type of data being sent through the [description], so that the content
     * can be converted to readable format when necessary. Such as in [com.spartancookie.hermeslogger.ui.fragments.InfoDetailedViewFragment]
     */
    val dataType: DataType? = null,

    /**
     * ## (Optional) Throwable.
     *
     * Contains the [Throwable] that the event refers to.
     */
    val throwable: Throwable? = null,

    /**
     * ## (Optional) List of tags associated with the event.
     *
     * List of tags that regard the event.
     */
    val tags: MutableList<String> = mutableListOf()

): Parcelable {

    /**
     * ## Date that the event was fired.
     */
    val creationDate: Date = Calendar.getInstance().time

    /**
     * ## Unique Identifier of the event.
     *
     * ID of the event with format [EventType.commentPrefix] + the number of the
     * event of that type.
     */
    var id: String = ""

}

/**
 * Filter the logs on the receiver according to [CustomSearch].
 */
internal fun List<EventDataHolder>.filterLogs(cs: CustomSearch) = filter { dataHolder -> dataHolder.contains(cs) }

/**
 * Get event identifier without the prefix.
 * @return Instance of [EventDataHolder]
 */
internal fun EventDataHolder.getEventTypeNumber(): String = id.filter { it.isDigit() }
package com.spartancookie.hermeslogger.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.core.models.EventDataHolder
import com.spartancookie.hermeslogger.core.models.getEventTypeNumber
import com.spartancookie.hermeslogger.utils.buildInfoContentOnly
import java.text.SimpleDateFormat
import java.util.*

/**
 * Share logic that is independent of the Android version
 */
internal object ShareHelperCommon {

    private val sharable: IFileSharable = sdk29AndUp { ShareHelperSdk29Up } ?: ShareHelperSdk28down

    private val headerContent
        get() = "Hermes logger stack dump captured " +
                "at ${SimpleDateFormat(ShareConstants.HEADER_DATE_FORMAT, Locale.getDefault()).format(Date())}"

    private val logCountContent
        get() = "Log count: Success:${infoHolder.getNumberOfLogsByType(EventType.Success)} " +
                "Info:${infoHolder.getNumberOfLogsByType(EventType.Info)} " +
                "Warning:${infoHolder.getNumberOfLogsByType(EventType.Warning)} " +
                "Error:${infoHolder.getNumberOfLogsByType(EventType.Error)} " +
                "Debug:${infoHolder.getNumberOfLogsByType(EventType.Debug)}"

    private val infoHolder = HermesHandler.infoHolder

    private val creationDateFormat = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault())

    fun enableShareFeature(context: Context) = sharable.initializeFeature(context)

    fun shareWholeLogStack(context: Context) {
        sharable.shareLogDump(context)
    }

    fun getDumpLogContent(): String = buildString {
        append("$headerContent\n")
        append("$logCountContent\n")

        for (event in infoHolder.eventList) {
            append("\n==============\n")

            append("${event.type.name.uppercase()}-${event.getEventTypeNumber()} ")
            append("at ${creationDateFormat.format(event.creationDate)}")

            if (event.tags.isNotEmpty()) {
                append("\n\nTags: ${event.tags}")
            }

            append("\nShort Message:${event.message}")

            event.description?.let { append("\nExtra Information:$it") }

            event.throwable?.let { append("\n\nThrowable:\n${it.stackTraceToString()}") }

            append("\n==============\n")
        }

        append(ShareConstants.FOOTER)
    }

    /**
     * Share a single log
     */
    fun shareLog(context: Context, event: EventDataHolder) {

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = ShareConstants.PLAIN_TEXT_INTENT_TYPE
            putExtra(Intent.EXTRA_SUBJECT, "${event.type} log captured at ${event.creationDate}- ${event.message}")
            putExtra(Intent.EXTRA_TEXT, buildInfoContentOnly(event))
        }
        (context as? Activity)?.run { startActivity(Intent.createChooser(intent, "Share log")) }
    }

}
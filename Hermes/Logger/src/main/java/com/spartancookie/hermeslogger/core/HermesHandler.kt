package com.spartancookie.hermeslogger.core

import com.spartancookie.hermeslogger.data.InfoHolder
import com.spartancookie.hermeslogger.models.EventDataHolder

internal object HermesHandler {

    val infoHolder = InfoHolder()

    /**
     * Add Log information to the queue
     * @param dataHolder Log information
     */
    fun add(dataHolder: EventDataHolder) {
        // Add log to the list of logs
        infoHolder.addInfo(dataHolder)
    }

    fun buildStats(): String = buildString {
        append("Events: ${infoHolder.totalEventsCount}, Shown: ${infoHolder.totalEventsShownCount}\n")
        append("-----------------------------\n")
        append("Success: ${infoHolder.getNumberOfLogsByType(EventType.Success)}\n")
        append("Verbose: ${infoHolder.getNumberOfLogsByType(EventType.Verbose)}\n")
        append("Debug: ${infoHolder.getNumberOfLogsByType(EventType.Debug)}\n")
        append("Info: ${infoHolder.getNumberOfLogsByType(EventType.Info)}\n")
        append("Warning: ${infoHolder.getNumberOfLogsByType(EventType.Warning)}\n")
        append("Error: ${infoHolder.getNumberOfLogsByType(EventType.Error)}\n")
        append("Wtf: ${infoHolder.getNumberOfLogsByType(EventType.Wtf)}")
    }
}
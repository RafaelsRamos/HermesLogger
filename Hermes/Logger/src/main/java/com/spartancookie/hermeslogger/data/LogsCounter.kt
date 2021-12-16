package com.spartancookie.hermeslogger.data

import com.spartancookie.hermeslogger.core.EventType
import java.util.concurrent.atomic.AtomicInteger

internal class LogsCounter: HashMap<EventType, AtomicInteger>() {

    private val DefaultMap = hashMapOf<EventType, AtomicInteger>().apply {
        // Loop through all types of logs, and add a connection in the HashMap with 0
        EventType.values().forEach { type ->
            put(type, AtomicInteger(0))
        }
    }

    init {
        putAll(DefaultMap)
    }

    /**
     * Reset the LogsCounter values, back to the default
     */
    fun reset() {
        keys.forEach { key -> this[key]!!.set(0) }
    }
}
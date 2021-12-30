package com.spartancookie.hermeslogger.data

import com.spartancookie.hermeslogger.core.EventType
import java.util.concurrent.atomic.AtomicInteger

internal class LogsCounter: HashMap<EventType, AtomicInteger>() {

    init {
        EventType.values().forEach { type ->
            put(type, AtomicInteger(0))
        }
    }

    /**
     * Reset the LogsCounter values, back to the default
     */
    fun reset() {
        keys.forEach { key -> this[key]!!.set(0) }
    }
}
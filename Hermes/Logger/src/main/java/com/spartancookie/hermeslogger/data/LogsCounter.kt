package com.spartancookie.hermeslogger.data

import com.spartancookie.hermeslogger.debugToaster.LogType
import java.util.concurrent.atomic.AtomicInteger

internal class LogsCounter: HashMap<LogType, AtomicInteger>() {

    private val DefaultMap = hashMapOf(
        LogType.Debug to AtomicInteger(0),
        LogType.Success to AtomicInteger(0),
        LogType.Warning to AtomicInteger(0),
        LogType.Error to AtomicInteger(0),
        LogType.Info to AtomicInteger(0)
    )

    init {
        putAll(DefaultMap)
    }

    fun reset() {
        keys.forEach { key -> this[key]!!.set(0) }
    }

}
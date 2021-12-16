package com.spartancookie.hermeslogger.filters

import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.models.EventDataHolder
import io.mockk.every
import io.mockk.mockk
import java.util.*

internal fun createEventDataHolder(
    times: Int,
    type: EventType,
    msg: String = "Placeholder message"
): List<EventDataHolder> {
    return mutableListOf<EventDataHolder>().apply {
        repeat(times) {
            add(EventDataHolder(type, msg))
        }
    }
}

internal fun createMockedDataHolder(eventType: EventType, eventMessage: String, eventCreationDate: Date): EventDataHolder {
    return mockk<EventDataHolder>().apply {
        every { creationDate } returns eventCreationDate
        every { type } returns eventType
        every { message } returns eventMessage
    }
}

internal fun List<EventDataHolder>.assertTypes(
    expectedNrSuccess: Int = 0,
    expectedNrDebug: Int = 0,
    expectedNrInfo: Int = 0,
    expectedNrWarning: Int = 0,
    expectedNrError: Int = 0
) {
    checkType(EventType.Success, expectedNrSuccess)
    checkType(EventType.Debug, expectedNrDebug)
    checkType(EventType.Info, expectedNrInfo)
    checkType(EventType.Warning, expectedNrWarning)
    checkType(EventType.Error, expectedNrError)
}

private fun List<EventDataHolder>.checkType(type: EventType, expectedNr: Int) {
    val realNr = count { it.type == type }
    assert(realNr == expectedNr) {
        "Expected ${type.name} logs: $realNr, found $expectedNr"
    }
}
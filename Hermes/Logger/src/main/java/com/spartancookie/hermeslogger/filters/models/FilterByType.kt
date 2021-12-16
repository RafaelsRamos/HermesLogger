package com.spartancookie.hermeslogger.filters.models

import com.spartancookie.hermeslogger.core.EventType
import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.models.EventDataHolder

internal data class FilterByType(
    val type: EventType,
    override val name: String = type.name.replaceFirstChar { it.uppercase() },
): Filter {

    override fun filter(unfilteredList: List<EventDataHolder>): List<EventDataHolder> {
        return unfilteredList.filter { it.type == type }
    }

}

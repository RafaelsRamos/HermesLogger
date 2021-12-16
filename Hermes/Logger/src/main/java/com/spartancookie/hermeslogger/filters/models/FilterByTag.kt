package com.spartancookie.hermeslogger.filters.models

import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.models.EventDataHolder

internal class FilterByTag(
    private val tag: String,
    override val name: String = tag,
) : Filter {

    override fun filter(unfilteredList: List<EventDataHolder>): List<EventDataHolder> {
        // TODO("Search for tags instead of this placeholder")
        return unfilteredList.filter { it.message.contains(tag) || it.extraInfo?.contains(tag) == true }
    }

}
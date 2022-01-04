package com.spartancookie.hermeslogger.filters.models

import com.spartancookie.hermeslogger.filters.Filter
import com.spartancookie.hermeslogger.core.models.EventDataHolder
import java.io.Serializable

internal class FilterByTag(private val tag: String, override val name: String = tag) : Filter, Serializable {

    override fun filter(unfilteredList: List<EventDataHolder>): List<EventDataHolder> {
        return unfilteredList.filter { it.message.contains(tag) || it.description?.contains(tag) == true || it.tags.contains(tag) }
    }

    override fun equals(other: Any?): Boolean {
        return other is FilterByTag && other.tag == tag && other.name == name
    }

    override fun hashCode(): Int = 31 * tag.hashCode() + name.hashCode()
}
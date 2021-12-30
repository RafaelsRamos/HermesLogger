package com.spartancookie.hermeslogger.filters

import com.spartancookie.hermeslogger.models.EventDataHolder

/**
 * Filter interface, to be implemented by the different filter types.
 */
internal interface Filter {

    /**
     * Filter name
     */
    val name: String

    /**
     * Method called to apply the filter to the set of events
     */
    fun filter(unfilteredList: List<EventDataHolder>): List<EventDataHolder>

}
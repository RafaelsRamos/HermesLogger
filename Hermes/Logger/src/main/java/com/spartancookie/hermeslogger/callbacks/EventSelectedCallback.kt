package com.spartancookie.hermeslogger.callbacks

import com.spartancookie.hermeslogger.models.EventDataHolder
import java.io.Serializable

/**
 * Interface to inform that a log was selected
 */
internal interface EventSelectedCallback: Serializable {

    /**
     * Triggered on [event] selected
     */
    fun onLogSelected(event: EventDataHolder)

}
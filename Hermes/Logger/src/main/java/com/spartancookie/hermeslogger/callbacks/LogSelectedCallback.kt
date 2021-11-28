package com.spartancookie.hermeslogger.callbacks

import com.spartancookie.hermeslogger.models.LogDataHolder
import java.io.Serializable

/**
 * Interface to inform that a log was selected
 */
internal interface LogSelectedCallback: Serializable {

    /**
     * Triggered on [log] selected
     */
    fun onLogSelected(log: LogDataHolder)

}
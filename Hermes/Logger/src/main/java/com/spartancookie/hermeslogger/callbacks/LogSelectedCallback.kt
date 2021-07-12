package com.spartancookie.hermeslogger.callbacks

import com.spartancookie.hermeslogger.models.LogDataHolder
import java.io.Serializable

/**
 * Interface to inform that a log was selected
 */
internal interface LogSelectedCallback: Serializable {

    /**
     * On log selected
     * @param log Log that was selected
     */
    fun onLogSelected(log: LogDataHolder)

}
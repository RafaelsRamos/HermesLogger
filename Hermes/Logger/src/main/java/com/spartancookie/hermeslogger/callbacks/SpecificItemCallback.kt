package com.spartancookie.hermeslogger.callbacks

import com.spartancookie.hermeslogger.models.LogDataHolder

/**
 * Callback for logs' Overview
 */
internal interface SpecificItemCallback {

    /**
     * Method invoked on Specific item clicked
     * @param item  Instance of [LogDataHolder] that contains the information of the log clicked
     */
    fun onSpecificItemClicked(item: LogDataHolder)

}
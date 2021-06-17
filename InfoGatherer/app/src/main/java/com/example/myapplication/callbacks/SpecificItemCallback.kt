package com.example.myapplication.callbacks

import com.example.myapplication.models.LogDataHolder

/**
 * Callback for logs' Overview
 */
interface SpecificItemCallback {

    /**
     * Method invoked on Specific item clicked
     * @param item  Instance of [LogDataHolder] that contains the information of the log clicked
     */
    fun onSpecificItemClicked(item: LogDataHolder)

}
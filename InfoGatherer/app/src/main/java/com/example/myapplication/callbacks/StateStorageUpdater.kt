package com.example.myapplication.callbacks

import com.example.myapplication.ui.search.CustomSearch

/**
 * Interface used to update state holders.
 */
internal interface StateStorageUpdater {

    /**
     * When the search content changes, this method is called to update string content on the
     * state holders
     * @param content new content
     */
    fun onCustomSearchChanged(customSearch: CustomSearch)

}
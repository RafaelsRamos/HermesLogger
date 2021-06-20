package com.example.myapplication.callbacks

/**
 * Interface used to update state holders.
 */
interface StateStorageUpdater {

    /**
     * When the search content changes, this method is called to update string content on the
     * state holders
     * @param content new content
     */
    fun onResultsFiltered(content: String)

}
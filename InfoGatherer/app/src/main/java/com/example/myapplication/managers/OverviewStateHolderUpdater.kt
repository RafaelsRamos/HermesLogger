package com.example.myapplication.managers

import com.example.myapplication.callbacks.StateStorageUpdater
import com.example.myapplication.utils.EMPTY_STRING

/**
 * Class responsible for storing the up-to-date states.
 */
class OverviewStateHolderUpdater: StateStorageUpdater {

    // current filter string on the search bar
    var currentContent = EMPTY_STRING

    override fun onResultsFiltered(content: String) {
        currentContent = content
    }

}
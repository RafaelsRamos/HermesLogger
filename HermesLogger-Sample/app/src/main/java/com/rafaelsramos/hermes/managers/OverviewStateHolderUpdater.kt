package com.rafaelsramos.hermes.managers

import com.rafaelsramos.hermes.callbacks.StateStorageUpdater
import com.rafaelsramos.hermes.ui.search.CustomSearch

/**
 * Class responsible for storing the up-to-date states.
 * states such as current search content, ect.
 */
internal class OverviewStateHolderUpdater: StateStorageUpdater {

    val currentContent get() =  customSearch.filterContent
    var customSearch = CustomSearch()

    override fun onCustomSearchChanged(customSearch: CustomSearch) {
        this.customSearch = customSearch
    }

}
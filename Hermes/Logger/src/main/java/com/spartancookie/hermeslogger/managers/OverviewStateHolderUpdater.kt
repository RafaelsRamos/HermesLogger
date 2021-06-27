package com.spartancookie.hermeslogger.managers

import com.spartancookie.hermeslogger.ui.search.CustomSearch

/**
 * Class responsible for storing the up-to-date states.
 * states such as current search content, ect.
 */
internal class OverviewStateHolderUpdater {

    val currentContent get() =  customSearch.filterContent
    var customSearch = CustomSearch()

    fun updateSearchContent(customSearch: CustomSearch) {
        this.customSearch = customSearch
    }

}
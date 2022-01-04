package com.spartancookie.hermeslogger.core

import com.spartancookie.hermeslogger.ui.search.CustomSearch

/**
 * Class responsible for storing the up-to-date states.
 * states such as current search content, ect.
 */
internal object OverviewStateHolder {

    val currentContent get() =  customSearch.filterContent
    var customSearch = CustomSearch()

    fun updateSearchContent(customSearch: CustomSearch) {
        OverviewStateHolder.customSearch = customSearch
    }

}
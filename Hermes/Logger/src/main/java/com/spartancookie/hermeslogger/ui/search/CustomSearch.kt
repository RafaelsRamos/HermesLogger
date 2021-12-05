package com.spartancookie.hermeslogger.ui.search

import com.spartancookie.hermeslogger.models.EventDataHolder
import com.spartancookie.hermeslogger.utils.EMPTY_STRING

/**
 * Class responsible for storing the state of the search
 */
internal class CustomSearch {

    /**
     * True if the logs are being filtered by case, False otherwise
     */
    var matchCase = false

    /**
     * Content by which the logs are being filtered
     */
    var filterContent = EMPTY_STRING

    /**
     * True if the logs are being filtered by regex, False otherwise
     *
     * Currently not in use
     */
    var isRegexEnabled = false

}

/**
 * Assess if a specific [EventDataHolder] can be displayed according to the rules on [CustomSearch]
 * @param cs [CustomSearch] that will be used to assess if the log that be displayed
 */
internal fun EventDataHolder.contains(cs: CustomSearch): Boolean {

    return if (cs.isRegexEnabled) {
        creationDate.toString().contains(cs.filterContent.toRegex())
                || extraInfo?.contains(cs.filterContent.toRegex()) == true
                || message.contains(cs.filterContent.toRegex())
                || id.contains(cs.filterContent.toRegex())
    } else {
        creationDate.toString().contains(cs.filterContent, !cs.matchCase)
                || extraInfo?.contains(cs.filterContent, !cs.matchCase) == true
                || message.contains(cs.filterContent, !cs.matchCase)
                || id.contains(cs.filterContent, !cs.matchCase)
    }

}
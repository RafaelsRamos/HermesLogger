package com.spartancookie.hermeslogger.ui.search

import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.utils.EMPTY_STRING

internal class CustomSearch {

    var matchCase = false

    var filterContent = EMPTY_STRING

    var isRegexEnabled = false

}

internal fun LogDataHolder.contains(cs: CustomSearch): Boolean {

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
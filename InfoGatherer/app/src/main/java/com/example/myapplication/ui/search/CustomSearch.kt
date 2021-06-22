package com.example.myapplication.ui.search

import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.utils.EMPTY_STRING

class CustomSearch {

    var ignoreCase = true

    var filterContent = EMPTY_STRING

    var isRegexEnabled = false

}

fun LogDataHolder.contains(cs: CustomSearch): Boolean {

    return if (cs.isRegexEnabled) {
        creationDate.toString().contains(cs.filterContent.toRegex())
                || extraInfo?.contains(cs.filterContent.toRegex()) == true
                || message.contains(cs.filterContent.toRegex())
                || id.contains(cs.filterContent.toRegex())
    } else {
        creationDate.toString().contains(cs.filterContent, cs.ignoreCase)
                || extraInfo?.contains(cs.filterContent, cs.ignoreCase) == true
                || message.contains(cs.filterContent, cs.ignoreCase)
                || id.contains(cs.filterContent, cs.ignoreCase)
    }

}
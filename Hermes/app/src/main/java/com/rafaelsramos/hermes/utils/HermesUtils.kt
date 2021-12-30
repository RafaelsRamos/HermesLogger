package com.rafaelsramos.hermes.utils

import com.spartancookie.hermeslogger.core.HermesConfigurations

fun String.toHTag(): String {
    val delimiter = HermesConfigurations.tagDelimiter
    return "$delimiter$this$delimiter"
}
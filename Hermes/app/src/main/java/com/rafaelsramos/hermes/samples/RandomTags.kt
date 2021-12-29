package com.rafaelsramos.hermes.samples

import kotlin.math.roundToInt

object RandomTags {

    private const val MAX_TAGS = 3

    private val tags = listOf(
        "Service-Call","Service-Success", "Service-Failure",
        "Exception",
        "App-Created", "App-Started", "App-Resumed", "App-Paused", "App-Stopped",
        "Trans-Activity", "Trans-Fragment",
        "Monitorization"
    )

    val getRandomTags: List<String> get() = tags.shuffled().take((Math.random().toFloat() * MAX_TAGS).roundToInt())
}
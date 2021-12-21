package com.spartancookie.tree

/**
 * Data class with event [message] and list of tags - [tags]
 */
internal data class HermesProcessedEvent(
    val message: String,
    val tags: List<String> = listOf()
)

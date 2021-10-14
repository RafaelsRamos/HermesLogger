package com.spartancookie.formatter

import java.lang.RuntimeException

private const val INVALID_JSON_MESSAGE = "Invalid json format"

/**
 * Assess if Json is valid.
 * TODO("Use an established library to do this")
 */
@Throws(RuntimeException::class)
internal fun isValidJson(jsonStr: String) {
    if (jsonStr.count { it == '{' } != jsonStr.count { it == '}' } ||
        jsonStr.count { it == '[' } != jsonStr.count { it == ']' } )
        throw RuntimeException(INVALID_JSON_MESSAGE)
}
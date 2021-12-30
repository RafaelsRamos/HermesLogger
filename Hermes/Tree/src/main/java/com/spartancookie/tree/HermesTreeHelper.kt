package com.spartancookie.tree

import android.os.Build
import com.spartancookie.hermeslogger.core.HermesConfigurations
import java.util.regex.Pattern

internal object HermesTreeHelper {

    private val ANONYMOUS_CLASS: Pattern = Pattern.compile("(\\$\\d+)+$")

    /**
     * COPIED FROM Timber
     *
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     *
     * Note: This will not be called if a [manual tag][.tag] was specified.
     */
    fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        // Tag length limit was removed in API 26.
        return if (tag.length <= 23 || Build.VERSION.SDK_INT >= 26) {
            tag
        } else {
            tag.substring(0, 23)
        }
    }

    /**
     * Get the associated tags by processing the contents of the given string
     * @param message Content that will be processed
     * @return Instance of HermesProcessedLog with the real message
     * and the List of tags, retrieved from the given string
     */
    fun getAssociatedTags(message: String): HermesProcessedEvent {

        val delimiter = HermesConfigurations.tagDelimiter

        // If one or less tag delimiters are found, return the default message
        if (message.count { it.toString() == delimiter } <= 1) {
            return HermesProcessedEvent(message)
        }

        // Split the content by the TAG_DELIMITER and sent them as a list
        val tags = message.split(delimiter).filter { it.isTag(message) }

        val realMessage = message.split(delimiter).last()
        return HermesProcessedEvent(realMessage, tags)
    }

    private fun String.isTag(wholeMessage: String): Boolean {
        val delimiter = HermesConfigurations.tagDelimiter
        return isNotBlank() && isNotEmpty() && wholeMessage.contains("$delimiter$this$delimiter")
    }
}
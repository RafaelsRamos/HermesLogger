package com.spartancookie.tree

import android.os.Build
import com.spartancookie.tree.HermesTimberConstants.TAG_DELIMITER
import com.spartancookie.tree.HermesTimberConstants.TAG_SECTION_DELIMITER
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
        // Split the message into TAGS section and message section
        val splitMessage = message.split(TAG_SECTION_DELIMITER)

        // If the splitMessage size is less than 2, the message has no tags.
        // In this case return HermesProcessedLog only with the message
        if (splitMessage.size < 2) {
            return HermesProcessedEvent(message)
        }

        // Split the content by the TAG_DELIMITER and sent them as a list
        val tags = splitMessage.first().split(TAG_DELIMITER).filter { it.isNotEmpty() && it.isNotBlank() }
        val realMessage = splitMessage[1]
        return HermesProcessedEvent(realMessage, tags)
    }

}
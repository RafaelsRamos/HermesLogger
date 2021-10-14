package com.spartancookie.formatter

/**
 * Interface to be implemented in classes that can be formatted (Prettified)
 */
interface IFormattable {

    /**
     * format [unformattedText] into a formatted string
     * @param unformattedText Unformatted string
     * @return  Formatted string
     */
    fun format(unformattedText: String): String

}
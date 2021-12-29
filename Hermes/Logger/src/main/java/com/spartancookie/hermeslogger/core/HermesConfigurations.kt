package com.spartancookie.hermeslogger.core

object HermesConfigurations {

    private const val DEFAULT_TAG_DELIMITER = "|"

    /**
     * State of the Hermes system
     */
    @JvmStatic
    var isEnabled = false
        internal set

    /**
     * Hermes delimited being used to wrap around the tag-string, in order to map them into an hermes tag.
     */
    @JvmStatic
    var tagDelimiter: String = DEFAULT_TAG_DELIMITER

    /**
     * Current instance of [SystemInfoBuildable]
     */
    internal var systemInfoBuildable : SystemInfoBuildable? = null

}
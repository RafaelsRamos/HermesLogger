package com.spartancookie.hermeslogger.core

/**
 * ## Object to access or manipulate Hermes configurations.
 */
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
     * Implementation of [SystemInfoBuildable], that can be used to take "SnapShots" and the system.
     */
    internal var systemInfoBuildable : SystemInfoBuildable? = null

    /**
     * Inform Hermes that the current the current environment is a debug environment or not.
     * If the environment is not a debug environment, no logs will be stored or shown.
     * [isDebugEnvironment] is True if the current environment is a debug environment, false otherwise
     */
    @JvmStatic
    fun initialize(isDebugEnvironment: Boolean) {
        isEnabled = isDebugEnvironment
    }

    /**
     * Add an implementation of [SystemInfoBuildable] so that the user can copy system info
     * when copying log information
     */
    @JvmStatic
    fun updateSystemInfo(systemInfoBuildable: SystemInfoBuildable) {
        this.systemInfoBuildable = systemInfoBuildable
    }

}
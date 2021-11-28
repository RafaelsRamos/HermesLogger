package com.spartancookie.hermeslogger.core

object HermesConfigurations {

    /**
     * State of the Hermes system
     */
    @JvmStatic
    var isEnabled = false
        internal set

    /**
     * Current instance of [SystemInfoBuildable]
     */
    internal var systemInfoBuildable : SystemInfoBuildable? = null

}
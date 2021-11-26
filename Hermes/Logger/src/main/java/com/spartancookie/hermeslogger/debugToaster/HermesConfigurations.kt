package com.spartancookie.hermeslogger.debugToaster

object HermesConfigurations {

    /**
     * State of the Hermes system
     */
    var isEnabled = false
        internal set

    /**
     * True if the toasts are to be shown, False otherwise
     */
    var areToastsEnabled = false
        internal set

    /**
     * Current instance of [SystemInfoBuildable]
     */
    internal var systemInfoBuildable : SystemInfoBuildable? = null

}
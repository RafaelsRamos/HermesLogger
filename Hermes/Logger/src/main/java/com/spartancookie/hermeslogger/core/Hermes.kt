package com.spartancookie.hermeslogger.core

object Hermes {

    @JvmStatic
    fun success() = HermesBuilder().apply { type = LogType.Success }

    @JvmStatic
    fun e() = HermesBuilder().apply { type = LogType.Error }

    @JvmStatic
    fun w() = HermesBuilder().apply { type = LogType.Warning }

    @JvmStatic
    fun d() = HermesBuilder().apply { type = LogType.Debug }

    @JvmStatic
    fun i() = HermesBuilder().apply { type = LogType.Info }

    /**
     * Inform Hermes class that the current the current environment is a debug environment or not.
     * If the environment is not a debug environment, no logs will be stored or shown.
     * @param isDebugEnvironment True if the current environment is a debug environment, false otherwise
     */
    @JvmStatic
    fun initialize(isDebugEnvironment: Boolean) {
        HermesConfigurations.isEnabled = isDebugEnvironment
    }

    /**
     * Add an implementation of [SystemInfoBuildable] so that the user can copy system info
     * when copying log information
     * @param systemInfoBuildable [SystemInfoBuildable] implementation
     */
    @JvmStatic
    fun updateSystemInfo(systemInfoBuildable: SystemInfoBuildable) {
        HermesConfigurations.systemInfoBuildable = systemInfoBuildable
    }

}
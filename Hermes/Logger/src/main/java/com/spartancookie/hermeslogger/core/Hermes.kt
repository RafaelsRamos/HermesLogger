package com.spartancookie.hermeslogger.core

object Hermes {

    /**
     * Create an instance of [HermesBuilder] with a priority of [LogType.Success]
     */
    @JvmStatic
    fun success(): HermesBuilder = HermesBuilder().apply { type = LogType.Success }

    /**
     * Create an instance of [HermesBuilder] with a priority of [LogType.Debug]
     */
    @JvmStatic
    fun d(): HermesBuilder = HermesBuilder().apply { type = LogType.Debug }

    /**
     * Create an instance of [HermesBuilder] with a priority of [LogType.Info]
     */
    @JvmStatic
    fun i(): HermesBuilder = HermesBuilder().apply { type = LogType.Info }

    /**
     * Create an instance of [HermesBuilder] with a priority of [LogType.Warning]
     */
    @JvmStatic
    fun w(): HermesBuilder = HermesBuilder().apply { type = LogType.Warning }


    /**
     * Create an instance of [HermesBuilder] with a priority of [LogType.Error]
     */
    @JvmStatic
    fun e(): HermesBuilder = HermesBuilder().apply { type = LogType.Error }

    /**
     * Inform Hermes class that the current the current environment is a debug environment or not.
     * If the environment is not a debug environment, no logs will be stored or shown.
     * [isDebugEnvironment] is True if the current environment is a debug environment, false otherwise
     */
    @JvmStatic
    fun initialize(isDebugEnvironment: Boolean) {
        HermesConfigurations.isEnabled = isDebugEnvironment
    }

    /**
     * Add an implementation of [SystemInfoBuildable] so that the user can copy system info
     * when copying log information
     */
    @JvmStatic
    fun updateSystemInfo(systemInfoBuildable: SystemInfoBuildable) {
        HermesConfigurations.systemInfoBuildable = systemInfoBuildable
    }

}
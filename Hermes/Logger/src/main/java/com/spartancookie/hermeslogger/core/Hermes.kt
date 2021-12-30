package com.spartancookie.hermeslogger.core

object Hermes {

    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Success]
     */
    @JvmStatic
    fun success(): HermesBuilder = HermesBuilder().apply { type = EventType.Success }

    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Verbose]
     */
    @JvmStatic
    fun v(): HermesBuilder = HermesBuilder().apply { type = EventType.Verbose }

    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Debug]
     */
    @JvmStatic
    fun d(): HermesBuilder = HermesBuilder().apply { type = EventType.Debug }

    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Info]
     */
    @JvmStatic
    fun i(): HermesBuilder = HermesBuilder().apply { type = EventType.Info }

    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Warning]
     */
    @JvmStatic
    fun w(): HermesBuilder = HermesBuilder().apply { type = EventType.Warning }


    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Error]
     */
    @JvmStatic
    fun e(): HermesBuilder = HermesBuilder().apply { type = EventType.Error }

    /**
     * Create an instance of [HermesBuilder] with a priority of [EventType.Wtf]
     */
    @JvmStatic
    fun wtf(): HermesBuilder = HermesBuilder().apply { type = EventType.Wtf }

    /**
     * Inform Hermes that the current the current environment is a debug environment or not.
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
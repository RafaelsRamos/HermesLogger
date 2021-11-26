package com.spartancookie.hermeslogger.debugToaster

import android.app.Activity
import java.lang.ref.WeakReference

object Hermes {

    const val LONG_TOAST_DURATION = 3500
    const val SHORT_TOAST_DURATION = 2000

    internal var actReference: WeakReference<Activity>? = null

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
     * Inform Toaster class that the current the current environment is a debug environment or not.
     * If the environment is not a debug environment, no logs will be stored or shown.
     * @param isDebugEnvironment True if the current environment is a debug environment, false otherwise
     */
    @JvmStatic
    @JvmOverloads
    fun initialize(isDebugEnvironment: Boolean, activity: Activity? = null) {

        HermesConfigurations.isEnabled = isDebugEnvironment

        if (isDebugEnvironment) {
            activity?.run { updateActivityReference(this) }
        }
    }

    /**
     * Update activity reference.
     * Having a reference to the activity is only required if the Toasts are to be shown.
     */
    @JvmStatic
    fun updateActivityReference(activity: Activity) {
        actReference = WeakReference(activity)
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

    /**
     * Clear queue of toasts for display
     */
    fun clearQueue() {
        HermesHandler.clearToastQueue()
    }
}
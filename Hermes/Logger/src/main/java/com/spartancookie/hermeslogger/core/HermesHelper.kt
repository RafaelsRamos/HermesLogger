package com.spartancookie.hermeslogger.core

/**
 * Fetches a "snapshot" of the system
 * @return String containing the snapshot of the system, if there is a valid instance of [SystemInfoBuildable]
 */
internal fun fetchSystemSnapshot(): String? {
    val buildable = HermesConfigurations.systemInfoBuildable
    return buildable?.buildSystemSnapshotInfo()
}
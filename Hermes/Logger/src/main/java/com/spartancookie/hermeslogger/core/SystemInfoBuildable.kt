package com.spartancookie.hermeslogger.core

/**
 * Implement this to build the Generic information that the user can copy to clipboard
 * @constructor Create empty Copy to clipboard generic info builder
 */
interface SystemInfoBuildable {

    /**
     * Get a snapshot of the system
     */
    fun buildSystemSnapshotInfo() : String

}
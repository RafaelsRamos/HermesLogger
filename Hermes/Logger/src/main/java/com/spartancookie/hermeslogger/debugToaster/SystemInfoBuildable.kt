package com.spartancookie.hermeslogger.debugToaster

/**
 * Implement this to build the Generic information that the user can copy to clipboard
 * @constructor Create empty Copy to clipboard generic info builder
 */
interface SystemInfoBuildable {
    fun buildSystemSnapshotInfo() : String
}
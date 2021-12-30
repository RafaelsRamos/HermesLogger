package com.spartancookie.hermeslogger.core

/**
 * Implement this to build the Generic information that the user can copy to clipboard
 */
interface SystemInfoBuildable {

    /**
     * Called when a new event is triggered, to take a snapshot of the system.
     *
     * It is advised so pass relevant system information, such as **Device model**, **App version**, **Device
     * display metrics**, **Language**, ect.
     */
    fun buildSystemSnapshotInfo() : String

}
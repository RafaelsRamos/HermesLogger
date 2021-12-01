package com.spartancookie.hermeslogger.share

import android.content.Context

/**
 * Interface for declaring the sharing of a file
 */
interface IFileSharable {

    /**
     * Make necessary initial Share setup
     */
    fun initializeFeature(context: Context) { }

    /**
     * Share entire log stack
     */
    fun shareLogDump(context: Context)

}
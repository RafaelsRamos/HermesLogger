package com.spartancookie.hermeslogger.callbacks

import java.io.Serializable

/**
 * Interface used to communicate with a fragment
 */
internal interface FragmentStateCallback: Serializable {

    /**
     * On Fragment dismissed
     */
    fun onFragmentDismissed()

}
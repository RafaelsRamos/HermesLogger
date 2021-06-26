package com.spartancookie.hermeslogger.callbacks

/**
 * Interface used to communicate with fragment from outside
 */
internal interface FragmentCommunicator {

    /**
     * Triggered on fragment detached
     */
    fun onFragmentDetached()

}
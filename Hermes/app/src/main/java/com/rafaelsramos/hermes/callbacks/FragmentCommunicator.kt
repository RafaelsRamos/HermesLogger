package com.rafaelsramos.hermes.callbacks

/**
 * Interface used to communicate with fragment from outside
 */
internal interface FragmentCommunicator {

    /**
     * Triggered on fragment detached
     */
    fun onFragmentDetached()

}
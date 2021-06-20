package com.example.myapplication.callbacks

/**
 * Interface used to communicate with fragment from outside
 */
interface FragmentCommunicator {

    /**
     * Triggered on fragment detached
     */
    fun onFragmentDetached()

}
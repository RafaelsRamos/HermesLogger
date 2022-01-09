package com.spartancookie.hermeslogger.commands

/**
 * ### Hermes Command interface, used to allow users to invoke commands.
 */
fun interface IHermesCommand {
    /**
     * Method invoked to execute the action.
     */
    fun execute()
}

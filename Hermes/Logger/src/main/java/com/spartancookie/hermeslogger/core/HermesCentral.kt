package com.spartancookie.hermeslogger.core

import androidx.lifecycle.LifecycleOwner
import com.spartancookie.hermeslogger.commands.FileManager
import com.spartancookie.hermeslogger.commands.models.HermesCommand

/**
 * Hermes class used to handle Commands.
 */
object HermesCentral {

    /**
     * ## Set [commands] as the only (transient) commands.
     *
     * Adding commands this way, clears all the currently active commands, and sets [commands] as the only ones.
     */
    @JvmStatic
    fun setCommands(commands: List<HermesCommand>) {
        FileManager.setFiles(commands)
    }

    /**
     * ## Add [commands] as transient commands.
     *
     * Adding commands this way, make them transient, until cleared manually.
     * Should be used for commands such as clearing SharedPreferences, or changing the App's language.
     */
    @JvmStatic
    fun addCommands(vararg commands: HermesCommand) {
        addCommands(commands.toList())
    }

    /**
     * ## Add [commands] as transient commands.
     *
     * Adding commands this way, make them transient, until cleared manually.
     * Should be used for commands such as clearing SharedPreferences, or changing the App's language.
     */
    @JvmStatic
    fun addCommands(commands: List<HermesCommand>) {
        FileManager.addFiles(commands)
    }

    /**
     * ## Add [commands] as temporary commands.
     *
     * Adding commands this way, make them temporary. These commands will be disposed of, when the [LifecycleOwner]
     * onStop is triggered.
     */
    @JvmStatic
    fun addTemporaryCommands(lifecycleOwner: LifecycleOwner, commands: List<HermesCommand>) {
        FileManager.addFiles(lifecycleOwner, commands)
    }

}
package com.spartancookie.hermeslogger.commands.models

import com.spartancookie.hermeslogger.commands.IHermesCommand

/**
 * ## Data class responsible for holding the information related to an Hermes command.
 * Constructor parameters:
 * - [name] - Name of the command. This will be used as a "File name", in Hermes Filing system;
 * - [command] - [IHermesCommand] implementation. [IHermesCommand.execute] method will be invoked when a used
 * presses the target command, on Hermes Filing system;
 * - (optional) [description] - Command description, that will be shown along with the command item itself;
 * - (optional) [path] - Path where the command will be stored into;
 */
data class HermesCommand @JvmOverloads constructor(
    /**
     * ### Command name.
     * Name that will be given to the command in Hermes Filing system.
     */
    override val name: String,
    /**
     * ### Concrete command - [IHermesCommand] implementation.
     * [IHermesCommand.execute] method will be invoked when a user presses the target command, on Hermes Filing system.
     */
    val command: IHermesCommand,
    /**
     * ### Command description
     * It is advised to provide this field, so that users can have more context on the command itself.
     * This information will be shown along with the command item itself.
     */
    val description: String? = null,
    override val path: String = "",
): HermesFile()

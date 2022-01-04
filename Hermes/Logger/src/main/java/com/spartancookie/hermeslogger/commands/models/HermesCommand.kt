package com.spartancookie.hermeslogger.commands.models

import com.spartancookie.hermeslogger.commands.IHermesCommand

data class HermesCommand @JvmOverloads constructor(
    override val name: String,
    val command: IHermesCommand,
    val description: String? = null,
    override val path: String = "",
): HermesFile()

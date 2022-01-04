package com.spartancookie.hermeslogger.commands.models

import com.spartancookie.hermeslogger.commands.IArrangeable

internal data class HermesFolder(
    override val name: String,
    val files: MutableList<IArrangeable> = mutableListOf()
): IArrangeable

internal fun MutableList<IArrangeable>.fetchFolder(folderName: String): HermesFolder? {
    return firstOrNull { it is HermesFolder && it.name == folderName } as? HermesFolder
}
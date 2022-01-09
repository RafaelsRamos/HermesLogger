package com.spartancookie.hermeslogger.commands.models

import com.spartancookie.hermeslogger.commands.IArrangeable

/**
 * ## Hermes Folder, where implementations for [IArrangeable] can be stored, such as [HermesFile] or other [HermesFolder].
 * [HermesFolder] works just like a regular Filing system's folder. Inside it, files, or other folders can be stored.
 */
internal data class HermesFolder(
    /**
     * ### Name of the Folder.
     * Name that will be used as a "File name", in Hermes Filing system.
     */
    override val name: String,
    val files: MutableList<IArrangeable> = mutableListOf()
): IArrangeable

internal fun MutableList<IArrangeable>.fetchFolder(folderName: String): HermesFolder? {
    return firstOrNull { it is HermesFolder && it.name == folderName } as? HermesFolder
}
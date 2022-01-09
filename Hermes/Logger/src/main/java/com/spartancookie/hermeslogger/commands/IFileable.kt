package com.spartancookie.hermeslogger.commands

/**
 * Interface that declares a class as being a File on our Filing system.
 */
internal interface IFileable {
    /**
     * ### Path where the file will be stored into.
     * If no path is given, the command will be stored on Root folder.
     */
    val path: String
}

internal val IFileable.folderNames get() = this.path.removeSuffix("/").split("/")
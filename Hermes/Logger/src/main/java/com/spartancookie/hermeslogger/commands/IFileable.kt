package com.spartancookie.hermeslogger.commands

/**
 * Interface that declares a class as being a File on our Filing system.
 */
internal interface IFileable {
    val path: String
}

internal val IFileable.folderNames get() = this.path.removeSuffix("/").split("/")
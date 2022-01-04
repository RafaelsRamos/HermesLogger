package com.spartancookie.hermeslogger.commands.models

import com.spartancookie.hermeslogger.commands.IArrangeable
import com.spartancookie.hermeslogger.commands.IFileable

/**
 * ## Hermes file base class.
 */
abstract class HermesFile : IArrangeable, IFileable {

    override fun equals(other: Any?): Boolean {
        return other is HermesFile && other.name == this.name && other.path == this.path
    }

    override fun hashCode(): Int = javaClass.hashCode()

}

internal fun List<HermesFile>.filterOut(files: List<HermesFile>): List<HermesFile> {
    return filter { file -> files.firstOrNull { file.name == it.name && file.path == it.path } == null }
}
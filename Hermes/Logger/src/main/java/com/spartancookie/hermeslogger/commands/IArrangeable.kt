package com.spartancookie.hermeslogger.commands

import com.spartancookie.hermeslogger.commands.models.HermesCommand
import com.spartancookie.hermeslogger.commands.models.HermesFolder

/**
 * Interface that identifies a class as being able to be arranged in a Filing system.
 */
internal interface IArrangeable {
    val name: String
}

internal fun MutableList<IArrangeable>.sortFoldersFirst(): List<IArrangeable> {
    return this.sortedWith { first, second ->
        when {
            first is HermesCommand && second is HermesFolder -> 1
            first is HermesFolder && second is HermesCommand -> -1
            else -> first.name.lowercase().compareTo(second.name.lowercase())
        }
    }
}
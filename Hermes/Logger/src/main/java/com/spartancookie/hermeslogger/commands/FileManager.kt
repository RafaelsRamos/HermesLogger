package com.spartancookie.hermeslogger.commands

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.commands.models.HermesFile
import com.spartancookie.hermeslogger.commands.models.HermesFolder
import com.spartancookie.hermeslogger.commands.models.fetchFolder
import com.spartancookie.hermeslogger.commands.models.filterOut

internal object FileManager {

    private val allFiles = mutableListOf<HermesFile>()

    val rootFolder = HermesFolder("Root")

    /**
     * [MutableLiveData] of type [HermesFolder], invoked when the [rootFolder] is updated.
     */
    //TODO("Make this a val, and fix the errors on CommandsFoldersTests.kt, caused by making this a val.")
    var rootFolderLiveData = MutableLiveData<HermesFolder>()

    fun setFiles(files: List<HermesFile>) {
        clearRootDirectory()
        addFiles(files)
    }

    /**
     * Add temporary files. Files are removed on [DefaultLifecycleObserver.onStop].
     */
    fun addFiles(owner: LifecycleOwner, files: List<HermesFile>) {
        addFiles(files)
        owner.lifecycle.addObserver(object: DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                removeFiles(files)
            }
        })
    }

    fun addFiles(files: List<HermesFile>) {
        // Distinct the given files by name and path.
        val uniqueFiles = files.distinctBy { it.name to it.path.removeSuffix("/") }

        // For each unique file, add it to the list (if not there already) and add it to the desired folder
        for (uniqueFile in uniqueFiles) {
            if (allFiles.contains(uniqueFile)) {
                continue
            }
            val targetFolder = buildFolder(rootFolder, uniqueFile.folderNames.toMutableList())
            targetFolder.files.add(uniqueFile)
            allFiles.add(uniqueFile)
        }
        rootFolderLiveData.postValue(rootFolder)
    }

    fun removeFiles(files: List<HermesFile>) {
        val _files = allFiles.filterOut(files)
        clearRootDirectory()
        addFiles(_files)
    }

    fun clearRootDirectory() {
        allFiles.clear()
        rootFolder.files.clear()
    }

    private fun buildFolder(targetFolder: HermesFolder, remainingFoldersNames: MutableList<String>): HermesFolder {
        val name = remainingFoldersNames.firstOrNull()

        // If the name is empty or blank, we're in the right folder.
        if (name == null || name.isEmpty() || name.isBlank()) {
            return targetFolder
        }

        // Remove the folder being treated out of the folders names.
        remainingFoldersNames.removeFirst()

        // Fetch an instance of HermesCommandFolder from the files in the targetFolder, that matches the name.
        // Null if returned, if none exists
        val existingFolder = targetFolder.files.fetchFolder(name)

        return if (existingFolder != null) {
            // If a folder was found, pass the "context" to inside that folder.
            buildFolder(existingFolder, remainingFoldersNames)
        } else {
            // If no folder was found, create one, and pass the "context" to inside that folder
            targetFolder.files.add(HermesFolder(name))
            buildFolder((targetFolder.files.last() as HermesFolder), remainingFoldersNames)
        }
    }
}
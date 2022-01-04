package com.spartancookie.hermeslogger.commands

import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.commands.models.HermesCommand
import com.spartancookie.hermeslogger.commands.models.HermesFolder
import io.mockk.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FilesTests {

    private val liveData = mockk<MutableLiveData<HermesFolder>>(relaxed = true)

    init {
        val commandsMock = mockkClass(FileManager::class)
        every { commandsMock.rootFolderLiveData } returns liveData
        every { liveData.postValue(any()) } just Runs
    }

    @Test
    fun `test root folder`() {
        FileManager.clearRootDirectory()

        val hermesCommands2 = listOf(
            HermesCommand(name = "title3", path = "Root", command = { }),
            HermesCommand(name = "title4", path = "root", command = { })
        )

        FileManager.addFiles(hermesCommands2)

        assert(FileManager.rootFolder.files.count() == 2)

        fetchFolder(FileManager.rootFolder, "root", 1)
    }

    @Test
    fun `test simple command and folder system`() {
        FileManager.clearRootDirectory()

        val hermesCommands = listOf(
            HermesCommand(name = "title1", path = "Shared Preferences/Test", command = { }),
            HermesCommand(name = "title2", path = "Random/Test2", command = { }),
        )

        val hermesCommands2 = listOf(
            HermesCommand(name = "title3", path = "Shared Preferences/Test2", command = { }),
            HermesCommand(name = "title4", path = "Code Guards/Test", command = { }),
            HermesCommand(name = "title5", path = "Code Guards", command = { })
        )

        FileManager.setFiles(hermesCommands)
        FileManager.addFiles(hermesCommands2)

        assert(FileManager.rootFolder.files.count() == 3)

        val prefsFolder = fetchFolder(FileManager.rootFolder, "Shared Preferences", 2)
        fetchFolder(prefsFolder, "Test", 1)
        fetchFolder(prefsFolder, "Test2", 1)
        fetchFolder(FileManager.rootFolder, "Code Guards", 2)
    }

    @Test
    fun `test commands and folders in same folder`() {
        val hermesCommands = listOf(
            HermesCommand(name = "title1", path = "level1-1/level2-1", command = { }),
            HermesCommand(name = "title2", path = "level1-1/level2-1", command = { }),
            HermesCommand(name = "title2", path = "level1-1", command = { }),
        )

        val hermesCommands2 = listOf(
            HermesCommand(name = "title3", path = "level1-1/", command = { }),
            HermesCommand(name = "title4", path = "level1-1/Test", command = { }),
            HermesCommand(name = "title5", path = "level1-1", command = { })
        )

        FileManager.setFiles(hermesCommands)
        FileManager.addFiles(hermesCommands2)

        assert(FileManager.rootFolder.files.count() == 1)
        fetchFolder(FileManager.rootFolder, "level1-1", 5)
    }

    @Test
    fun `test crazy command folder system`() {
        FileManager.clearRootDirectory()

        val hermesCommands = listOf(
            HermesCommand(name = "title1", path = "A1/B1/C1/D1/E1", command = { }),
            HermesCommand(name = "title2", path = "A1/B1/C1/D2/E2/F2/", command = { }),
            HermesCommand(name = "title3", path = "A1/", command = { }),
            HermesCommand(name = "title4", path = "A1", command = { }),
            HermesCommand(name = "title5", path = "", command = { }),
            HermesCommand(name = "title6", command = { }),
            HermesCommand(name = "title7", path = "A1/B1", command = { }),
        )

        FileManager.setFiles(hermesCommands)

        assert(FileManager.rootFolder.files.count() == 3)
        val A1Folder = fetchFolder(FileManager.rootFolder, "A1", 3)
        val B1Folder = fetchFolder(A1Folder, "B1", 2)
        val C1Folder = fetchFolder(B1Folder, "C1", 2)
        val D1Folder = fetchFolder(C1Folder, "D1", 1)
        val E1Folder = fetchFolder(D1Folder, "E1", 1)

        val D2Folder = fetchFolder(C1Folder, "D2", 1)
        val E2Folder = fetchFolder(D2Folder, "E2", 1)
        val F2Folder = fetchFolder(E2Folder, "F2", 1)
    }

    @Test
    fun `test file replication`() {
        FileManager.clearRootDirectory()

        val hermesCommands = listOf(
            HermesCommand(name = "title1", path = "A1/", command = { }),
            HermesCommand(name = "title1", path = "A1", command = { }),
            HermesCommand(name = "title3", path = "A1/", command = { }),
        )

        FileManager.setFiles(hermesCommands)

        assert(FileManager.rootFolder.files.count() == 1) {
            "Expected 1 file, found ${FileManager.rootFolder.files.count()}"
        }
        fetchFolder(FileManager.rootFolder, "A1", 2)
    }


    private fun fetchFolder(baseFolder: HermesFolder, targetFolderName: String, expectedNrOfFilesInFolder: Int): HermesFolder {
        val folder = baseFolder.files.firstOrNull { it is HermesFolder&& it.name == targetFolderName } as? HermesFolder
        assert(folder != null && folder.files.count() == expectedNrOfFilesInFolder) {
            "On $targetFolderName, expected $expectedNrOfFilesInFolder found ${folder?.files?.count()}"
        }
        return folder!!
    }
}
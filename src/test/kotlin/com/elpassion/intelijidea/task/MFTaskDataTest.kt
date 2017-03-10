package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.util.mfFilename
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class IntegrationTest {

    @get:Rule
    var temporaryFolder = TemporaryFolder()

    @Test
    fun shouldTaskDataInvalidWhenBuildCommandIsEmpty() {
        val taskData = createTaskData(buildCommand = "")

        assertFalse(taskData.isValid())
    }

    @Test
    fun shouldTaskDataInvalidWhenTaskNameIsEmpty() {
        val taskData = createTaskData(taskName = "")

        assertFalse(taskData.isValid())
    }

    @Test
    fun shouldTaskDataInvalidWhenPathIsEmpty() {
        val taskDataWithoutFolder = createTaskData().copy(mainframerPath = "")

        assertFalse(taskDataWithoutFolder.isValid())
    }

    @Test
    fun shouldTaskDataInvalidWhenFileOnPathDoesNotExist() {
        val taskDataWithoutFolder = createTaskData()

        assertFalse(taskDataWithoutFolder.isValid())
    }

    @Test
    fun shouldTaskDataInvalidWhenFileIsADirectory() {
        val taskDataWithoutFolder = createTaskData().copy(mainframerPath = temporaryFolder.root.absolutePath)

        assertFalse(taskDataWithoutFolder.isValid())
    }

    @Test
    fun shouldTaskDataInvalidWhenFileDoesNotHaveExecutablePremissions() {
        val taskDataWithoutFolder = createTaskData().copy()
        temporaryFolder.newFile(mfFilename)

        assertFalse(taskDataWithoutFolder.isValid())
    }

    @Test
    fun shouldTaskDataValidWhenAllDataIsValid() {
        val taskDataWithoutFolder = createTaskData()
        temporaryFolder.newFile(mfFilename).setExecutable(true)

        assertTrue(taskDataWithoutFolder.isValid())
    }

    private fun createTaskData(buildCommand: String = "./gradlew", taskName: String = "build"): MFTaskData {
        val folderPath = File(temporaryFolder.root.absolutePath, mfFilename).absolutePath
        return MFTaskData(buildCommand, taskName, folderPath)
    }
}
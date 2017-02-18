package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.util.mfFilename
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

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
    fun shouldTaskDataValidWhenAllDataIsValid() {
        val taskDataWithoutFolder = createTaskData()

        assertTrue(taskDataWithoutFolder.isValid())
    }

    private fun createTaskData(buildCommand: String = "./gradlew", taskName: String = "build"): MFTaskData {
        val folderPath = temporaryFolder.root.path
        temporaryFolder.newFile(mfFilename)
        return MFTaskData(folderPath, buildCommand, taskName)
    }
}
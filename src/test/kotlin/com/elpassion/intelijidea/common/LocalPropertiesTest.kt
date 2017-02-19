package com.elpassion.intelijidea.common

import org.assertj.core.api.Assertions
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LocalPropertiesTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()
    val localProperties by lazy { LocalProperties(temporaryFolder.root.path) }
    val localPropertiesFile by lazy { File(temporaryFolder.root.path, "local.properties") }

    @Test
    fun shouldReturnEmptyMachineNameIfLocalPropertiesDoesNotExists() {
        assertNull(localProperties.readRemoteMachineName())
    }

    @Test
    fun shouldReturnEmptyMachineNameThereIsNoRelevantProperty() {
        temporaryFolder.newFile("local.properties")
        assertNull(localProperties.readRemoteMachineName())
    }

    @Test
    fun shouldReturnRemoteMachineNameFromFile() {
        temporaryFolder.newFile("local.properties").writeText("remote_build.machine=not_local")
        assertEquals("not_local", localProperties.readRemoteMachineName())
    }

    @Test
    fun shouldReturnCorrectRemoteMachineNameFromFile() {
        temporaryFolder.newFile("local.properties").writeText("remote_build.machine=not_even_remote")
        assertEquals("not_even_remote", localProperties.readRemoteMachineName())
    }

    @Test
    fun shouldCreateLocalPropertiesFileWhileWritingToIt() {
        localProperties.writeRemoteMachineName("not_local")

        assertTrue(localPropertiesFile.exists())
        assertTrue(localPropertiesFile.readLines().any { it == "remote_build.machine=not_local" })
    }

    @Test
    fun shouldWriteCorrectValueToLocalProperties() {
        localProperties.writeRemoteMachineName("not_even_remote")
        assertTrue(localPropertiesFile.readLines().any { it == "remote_build.machine=not_even_remote" })
    }

    @Test
    fun shouldAppendRemoteMachineNameToExistingProperties() {
        setupLocalPropertiesWithText("some_property=secret_value")
        localProperties.writeRemoteMachineName("remote_name")

        Assertions.assertThat(localPropertiesFile.readLines())
                .contains("some_property=secret_value", "remote_build.machine=remote_name")
    }

    private fun setupLocalPropertiesWithText(text: String) {
        temporaryFolder.newFile("local.properties").writeText(text)
    }
}
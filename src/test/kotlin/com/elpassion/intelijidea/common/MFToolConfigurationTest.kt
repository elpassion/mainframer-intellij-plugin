package com.elpassion.intelijidea.common

import org.assertj.core.api.Assertions
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class MFToolConfigurationTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()
    val toolConfiguration by lazy { MFToolConfiguration(temporaryFolder.root.path) }
    val configurationFile by lazy { File(File(temporaryFolder.root.path, ".mainframer"), "config") }

    @Before
    fun setUp() {
        temporaryFolder.newFolder(".mainframer")
    }

    @Test
    fun shouldReturnEmptyMachineNameIfLocalPropertiesDoesNotExists() {
        assertNull(toolConfiguration.readRemoteMachineName())
    }

    @Test
    fun shouldReturnEmptyMachineNameThereIsNoRelevantProperty() {
        configurationFile.writeText("someText")
        assertNull(toolConfiguration.readRemoteMachineName())
    }

    @Test
    fun shouldReturnRemoteMachineNameFromFile() {
        configurationFile.writeText("remote_machine=not_local")
        assertEquals("not_local", toolConfiguration.readRemoteMachineName())
    }

    @Test
    fun shouldReturnCorrectRemoteMachineNameFromFile() {
        configurationFile.writeText("remote_machine=not_even_remote")
        assertEquals("not_even_remote", toolConfiguration.readRemoteMachineName())
    }

    @Test
    fun shouldCreateLocalPropertiesFileWhileWritingToIt() {
        toolConfiguration.writeRemoteMachineName("not_local")

        assertTrue(configurationFile.exists())
        assertTrue(configurationFile.readLines().any { it == "remote_machine=not_local" })
    }

    @Test
    fun shouldWriteCorrectValueToLocalProperties() {
        toolConfiguration.writeRemoteMachineName("not_even_remote")
        assertTrue(configurationFile.readLines().any { it == "remote_machine=not_even_remote" })
    }

    @Test
    fun shouldAppendRemoteMachineNameToExistingProperties() {
        configurationFile.writeText("some_property=secret_value")
        toolConfiguration.writeRemoteMachineName("remote_name")

        Assertions.assertThat(configurationFile.readLines())
                .contains("some_property=secret_value", "remote_machine=remote_name")
    }
}
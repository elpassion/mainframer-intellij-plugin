package com.elpassion.intelijidea.configuration

import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.command.impl.DummyProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MFRunnerConfigurationTest {

    private val confFactory = MFConfigurationFactory(MFRunConfigurationType())
    private val project = DummyProject.getInstance()

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenDataIsNullOnCheckConfiguration() {
        val exception = assertThrows<RuntimeConfigurationError>(RuntimeConfigurationError::class.java) {
            MFRunnerConfiguration(project, confFactory, "")
                    .apply { data = null }
                    .checkConfiguration()
        }
        assertEquals("Configuration incorrect", exception.message)
    }
    
    
    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        val exception = assertThrows<RuntimeConfigurationError>(RuntimeConfigurationError::class.java) {
            MFRunnerConfiguration(project, confFactory, "")
                    .apply { data = mfRunnerConfigurationData(buildCommand = "") }
                    .checkConfiguration()
        }
        assertEquals("Build command cannot be empty", exception.message)
    }

    private fun mfRunnerConfigurationData(buildCommand: String = "buildCommand",
                                          taskName: String = "taskName",
                                          mainframerPath: String? = "path") = MFRunnerConfigurationData(buildCommand, taskName, mainframerPath)

}
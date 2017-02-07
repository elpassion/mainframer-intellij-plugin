package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.configuration.common.assertThrows
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.command.impl.DummyProject
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MFRunnerConfigurationTest {

    private val confFactory = MFConfigurationFactory(MFRunConfigurationType())
    private val project = DummyProject.getInstance()

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenDataIsNullOnCheckConfiguration() {
        val exception = assertThrows(RuntimeConfigurationError::class.java) {
            MFRunnerConfiguration(project, confFactory, "")
                    .apply { data = null }
                    .checkConfiguration()
        }
        assertEquals("Configuration incorrect", exception.message)
    }


    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        val exception = assertThrows(RuntimeConfigurationError::class.java) {
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
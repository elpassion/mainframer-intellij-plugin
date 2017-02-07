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
        assertExceptionMessage(
                expectedMessage = "Configuration incorrect",
                mfRunnerConfigurationData = null
        )
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        assertExceptionMessage(
                expectedMessage = "Build command cannot be empty",
                mfRunnerConfigurationData = mfRunnerConfigurationData(buildCommand = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenTaskNameIsBlankOnCheckConfiguration() {
        assertExceptionMessage(
                expectedMessage = "Task name cannot be empty",
                mfRunnerConfigurationData = mfRunnerConfigurationData(taskName = ""))
    }

    private fun assertExceptionMessage(expectedMessage: String, mfRunnerConfigurationData: MFRunnerConfigurationData?) {
        val exception = assertThrows(RuntimeConfigurationError::class.java) {
            MFRunnerConfiguration(project, confFactory, "")
                    .apply { data = mfRunnerConfigurationData }
                    .checkConfiguration()
        }
        assertEquals(expectedMessage, exception.message)
    }

    private fun mfRunnerConfigurationData(buildCommand: String = "buildCommand",
                                          taskName: String = "taskName",
                                          mainframerPath: String? = "path") = MFRunnerConfigurationData(buildCommand, taskName, mainframerPath)

}
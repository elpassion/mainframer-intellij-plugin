package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.configuration.common.assertThrows
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.command.impl.DummyProject
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import junit.framework.TestCase.assertEquals
import org.jdom.Element
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class MFRunnerConfigurationTest {

    private val confFactory = MFConfigurationFactory(MFRunConfigurationType())
    private val project = DummyProject.getInstance()
    private val element = mock<Element>()

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenDataIsNullOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Configuration incorrect",
                mfRunnerConfigurationData = null)
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Build command cannot be empty",
                mfRunnerConfigurationData = mfRunnerConfigurationData(buildCommand = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenTaskNameIsBlankOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Task name cannot be empty",
                mfRunnerConfigurationData = mfRunnerConfigurationData(taskName = ""))
    }

    @Test
    fun shouldNotSetAttributeValueWhenDataIsNullOnWriteExternal() {
        MFRunnerConfiguration(project, confFactory, "").run {
            data = null
            writeExternal(element)
            verify(element, never()).setAttribute(any(), any())
        }
    }

    private fun assertExceptionMessageOnCheckConfiguration(expectedMessage: String, mfRunnerConfigurationData: MFRunnerConfigurationData?) {
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
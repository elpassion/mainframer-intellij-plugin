package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.configuration.common.assertThrows
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.jdom.Element
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class MFRunnerConfigurationTest {

    private val confFactory = MFConfigurationFactory(MFRunConfigurationType())
    private val project = mock<Project>()
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
        mfRunnerConfiguration().run {
            data = null
            writeExternal(element)
            verify(element, never()).setAttribute(any(), any())
        }
    }

    @Test
    fun shouldSetAttributeValueEqualToDataFieldOnWriteExternal() {
        mfRunnerConfiguration().run {
            data = mfRunnerConfigurationData(buildCommand = "BuildCommand", taskName = "TaskName", mainframerPath = null)
            writeExternal(element)
            verify(element).setAttribute(any(), eq("{\"build_command\":\"BuildCommand\",\"task_name\":\"TaskName\",\"mainframer_path\":null}"))
        }
    }

    @Test
    fun shouldSetDefaultDataObjectWhenGetAttributeValueReturnsNullOnReadExternal() {
        whenever(project.basePath).thenReturn("basePath")
        assertReadExternalValue(
                expectedMfRunnerConfigurationData = mfRunnerConfigurationData(buildCommand = "./gradlew", taskName = "build", mainframerPath = "basePath"),
                savedMfRunnerConfigurationData = null)
    }

    @Test
    fun shouldSetObjectFromGetAttributeValueOnReadExternal() {
        assertReadExternalValue(
                expectedMfRunnerConfigurationData = mfRunnerConfigurationData(buildCommand = "build_command", taskName = "task_name", mainframerPath = "path"),
                savedMfRunnerConfigurationData = "{\"build_command\":\"build_command\",\"task_name\":\"task_name\",\"mainframer_path\":\"path\"}")
    }

    @Test
    fun shouldThrowExecutionExceptionWhenDataIsNullOnGetState() {
        val exception = assertThrows(ExecutionException::class.java) {
            mfRunnerConfiguration()
                    .apply { data = null }
                    .getState(mock(), mock())
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
    }

    @Test
    fun shouldThrowExecutionExceptionWhenMfFileIsNotAvailableOnGetState() {
        var isToolNotFoundErrorShown = false
        var shownMainFramerPath: String? = null
        val exception = assertThrows(ExecutionException::class.java) {
            mfRunnerConfiguration {
                isToolNotFoundErrorShown = true
                shownMainFramerPath = it
            }.apply {
                data = mfRunnerConfigurationData(mainframerPath = "mainFramerPath")
            }.getState(mock(), mock())
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
        assertTrue(isToolNotFoundErrorShown)
        assertEquals("mainFramerPath", shownMainFramerPath)
    }

    private fun assertExceptionMessageOnCheckConfiguration(expectedMessage: String, mfRunnerConfigurationData: MFRunnerConfigurationData?) {
        val exception = assertThrows(RuntimeConfigurationError::class.java) {
            mfRunnerConfiguration()
                    .apply { data = mfRunnerConfigurationData }
                    .checkConfiguration()
        }
        assertEquals(expectedMessage, exception.message)
    }

    private fun assertReadExternalValue(expectedMfRunnerConfigurationData: MFRunnerConfigurationData, savedMfRunnerConfigurationData: String?) {
        whenever(element.getAttributeValue(any())).thenReturn(savedMfRunnerConfigurationData)
        mfRunnerConfiguration().run {
            readExternal(element)
            assertEquals(expectedMfRunnerConfigurationData, data)
        }
    }

    private fun mfRunnerConfiguration(showToolNotFoundError: (String?) -> Unit = {}) = MFRunnerConfiguration(project, confFactory, "", showToolNotFoundError)

    private fun mfRunnerConfigurationData(buildCommand: String = "buildCommand",
                                          taskName: String = "taskName",
                                          mainframerPath: String? = "path") = MFRunnerConfigurationData(buildCommand, taskName, mainframerPath)

}
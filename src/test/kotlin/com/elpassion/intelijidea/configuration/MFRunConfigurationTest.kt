package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.assertThrows
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.jdom.Element
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class MFRunConfigurationTest {

    private val confFactory = MFConfigurationFactory(MFRunConfigurationType())
    private val project = mock<Project>()
    private val element = mock<Element>()

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Build command cannot be empty",
                mfRunConfigurationData = mfRunConfigurationData(buildCommand = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenTaskNameIsBlankOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Task name cannot be empty",
                mfRunConfigurationData = mfRunConfigurationData(taskName = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenScriptPathIsInvalidOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Mainframer tool cannot be found",
                mfRunConfigurationData = mfRunConfigurationData(mainframerPath = ""))
    }

    @Test
    fun shouldNotSetAttributeValueWhenDataIsNullOnWriteExternal() {
        mfRunConfiguration().run {
            data = null
            writeExternal(element)
            verify(element, never()).setAttribute(any(), any())
        }
    }

    @Test
    fun shouldSetAttributeValueEqualToDataFieldOnWriteExternal() {
        mfRunConfiguration().run {
            data = mfRunConfigurationData(buildCommand = "BuildCommand", taskName = "TaskName", mainframerPath = null)
            writeExternal(element)
            verify(element).setAttribute(any(), eq("{\"build_command\":\"BuildCommand\",\"task_name\":\"TaskName\",\"mainframer_path\":null}"))
        }
    }

    @Test
    fun shouldSetDefaultDataObjectWhenGetAttributeValueReturnsNullOnReadExternal() {
        whenever(project.basePath).thenReturn("basePath")
        assertReadExternalValue(
                expectedMfRunConfigurationData = mfRunConfigurationData(buildCommand = "./gradlew", taskName = "build", mainframerPath = "basePath"),
                savedMfRunConfigurationData = null)
    }

    @Test
    fun shouldSetObjectFromGetAttributeValueOnReadExternal() {
        assertReadExternalValue(
                expectedMfRunConfigurationData = mfRunConfigurationData(buildCommand = "build_command", taskName = "task_name", mainframerPath = "path"),
                savedMfRunConfigurationData = "{\"build_command\":\"build_command\",\"task_name\":\"task_name\",\"mainframer_path\":\"path\"}")
    }

    @Test
    fun shouldReturnFalseOnIsCompileBeforeLaunchAddedByDefault() {
        assertFalse(mfRunConfiguration().isCompileBeforeLaunchAddedByDefault)
    }

    private fun assertExceptionMessageOnCheckConfiguration(expectedMessage: String, mfRunConfigurationData: MFRunConfigurationData?) {
        val exception = assertThrows<RuntimeConfigurationError> {
            mfRunConfiguration()
                    .apply { data = mfRunConfigurationData }
                    .checkConfiguration()
        }
        assertEquals(expectedMessage, exception.message)
    }

    private fun assertReadExternalValue(expectedMfRunConfigurationData: MFRunConfigurationData, savedMfRunConfigurationData: String?) {
        whenever(element.getAttributeValue(any())).thenReturn(savedMfRunConfigurationData)
        mfRunConfiguration().run {
            readExternal(element)
            assertEquals(expectedMfRunConfigurationData, data)
        }
    }

    private fun mfRunConfiguration() = MFRunConfiguration(project, confFactory, "")

    private fun mfRunConfigurationData(buildCommand: String = "buildCommand",
                                       taskName: String = "taskName",
                                       mainframerPath: String? = "path") = MFRunConfigurationData(buildCommand, taskName, mainframerPath)

}
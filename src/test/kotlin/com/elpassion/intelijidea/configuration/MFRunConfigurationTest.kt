package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.assertThrows
import com.elpassion.intelijidea.task.MFTaskData
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
                taskData = mfTaskData(buildCommand = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenScriptPathIsInvalidOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Mainframer tool cannot be found",
                taskData = mfTaskData(mainframerPath = ""))
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
            data = mfTaskData(buildCommand = "BuildCommand", mainframerPath = "path")
            writeExternal(element)
            verify(element).setAttribute(any(), eq("{\"build_command\":\"BuildCommand\",\"mainframer_path\":\"path\"}"))
        }
    }

    @Test
    fun shouldSetDefaultDataObjectWhenGetAttributeValueReturnsNullOnReadExternal() {
        whenever(project.basePath).thenReturn("basePath")
        assertReadExternalValue(
                expectedMFTaskData = mfTaskData(buildCommand = "./gradlew", mainframerPath = "basePath"),
                savedMfRunConfigurationData = null)
    }

    @Test
    fun shouldSetObjectFromGetAttributeValueOnReadExternal() {
        assertReadExternalValue(
                expectedMFTaskData = mfTaskData(buildCommand = "build_command", mainframerPath = "path"),
                savedMfRunConfigurationData = "{\"build_command\":\"build_command\",\"task_name\":\"task_name\",\"mainframer_path\":\"path\"}")
    }

    @Test
    fun shouldReturnFalseOnIsCompileBeforeLaunchAddedByDefault() {
        assertFalse(mfRunConfiguration().isCompileBeforeLaunchAddedByDefault)
    }

    private fun assertExceptionMessageOnCheckConfiguration(expectedMessage: String, taskData: MFTaskData?) {
        val exception = assertThrows<RuntimeConfigurationError> {
            mfRunConfiguration()
                    .apply { data = taskData }
                    .checkConfiguration()
        }
        assertEquals(expectedMessage, exception.message)
    }

    private fun assertReadExternalValue(expectedMFTaskData: MFTaskData, savedMfRunConfigurationData: String?) {
        whenever(element.getAttributeValue(any())).thenReturn(savedMfRunConfigurationData)
        mfRunConfiguration().run {
            readExternal(element)
            assertEquals(expectedMFTaskData, data)
        }
    }

    private fun mfRunConfiguration() = MFRunConfiguration(project, confFactory, "")

    private fun mfTaskData(buildCommand: String = "buildCommand",
                           mainframerPath: String = "path") = MFTaskData(mainframerPath = mainframerPath, buildCommand = buildCommand)
}
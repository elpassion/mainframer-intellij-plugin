package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.assertThrows
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.jdom.Element

class MainframerRunConfigurationTest {

    private val confFactory = MainframerConfigurationFactory(MainframerConfigurationType())
    private val project = mock<Project>()
    private val element = mock<Element>()

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenBuildCommandIsBlankOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Build command cannot be empty",
                taskData = createTaskData(buildCommand = ""))
    }

    @Test
    fun shouldThrowRuntimeConfigurationErrorWhenScriptPathIsInvalidOnCheckConfiguration() {
        assertExceptionMessageOnCheckConfiguration(
                expectedMessage = "Cannot find Mainframer script in path",
                taskData = createTaskData(mainframerPath = ""))
    }

    @Test
    fun shouldReturnFalseOnIsCompileBeforeLaunchAddedByDefault() {
        assertFalse(createRunConfiguration().isCompileBeforeLaunchAddedByDefault)
    }

    @Test
    fun shouldNotSetAttributeValueWhenDataIsNullOnWriteExternal() {
        createRunConfiguration().run {
            data = null
            writeExternal(element)
            verify(element, never()).setAttribute(any(), any())
        }
    }

    @Test
    fun shouldSetAttributeValueEqualToDataFieldOnWriteExternal() {
        createRunConfiguration().run {
            data = createTaskData(buildCommand = "BuildCommand", mainframerPath = "path")
            writeExternal(element)
            verify(element).setAttribute(any(), eq("{\"build_command\":\"BuildCommand\",\"mainframer_path\":\"path\"}"))
        }
    }

    @Test
    fun shouldSetObjectFromGetAttributeValueOnReadExternal() {
        assertReadExternalValue(
                expectedTaskData = createTaskData(buildCommand = "build_command", mainframerPath = "path"),
                savedRunConfigurationData = "{\"build_command\":\"build_command\",\"task_name\":\"task_name\",\"mainframer_path\":\"path\"}")
    }

    private fun assertExceptionMessageOnCheckConfiguration(expectedMessage: String, taskData: TaskData?) {
        val exception = assertThrows<RuntimeConfigurationError> {
            createRunConfiguration()
                    .apply { data = taskData }
                    .checkConfiguration()
        }
        assertEquals(expectedMessage, exception.message)
    }

    private fun assertReadExternalValue(expectedTaskData: TaskData, savedRunConfigurationData: String?) {
        whenever(element.getAttributeValue(any())).thenReturn(savedRunConfigurationData)
        createRunConfiguration().run {
            readExternal(element)
            assertEquals(expectedTaskData, data)
        }
    }

    private fun createRunConfiguration() = MainframerRunConfiguration(project, confFactory, "")

    private fun createTaskData(buildCommand: String = "buildCommand",
                               mainframerPath: String = "path") = TaskData(mainframerPath = mainframerPath, buildCommand = buildCommand)
}

package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.common.ToolConfiguration
import com.elpassion.mainframerplugin.common.assertThrows
import com.elpassion.mainframerplugin.task.ui.TaskDefaultSettingsPanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.swing.JTextField

class TaskDefaultSettingsPanelTest {

    private val toolConfiguration = mock<ToolConfiguration>()

    @Test
    fun `should throw configuration exception when build command is empty`() {
        val settingsPanel = setupPanel(buildCommand = "")
        assertThrows<ConfigurationException> {
            settingsPanel.apply()
        }
    }

    @Test
    fun `should throw configuration exception when mainframer path is not valid`() {
        val settingsPanel = setupPanel(mainframerPath = "")
        assertThrows<ConfigurationException> {
            settingsPanel.apply()
        }
    }

    @Test
    fun `should throw configuration exception when remote machine name is empty`() {
        val settingsPanel = setupPanel(remoteMachineName = "")
        assertThrows<ConfigurationException> {
            settingsPanel.apply()
        }
    }

    @Test
    fun `should reset build command field`() {
        val settingsPanel = setupPanel(buildCommand = "build command")
        settingsPanel.buildCommandField.text = "other command"
        settingsPanel.reset()

        assertEquals("build command", settingsPanel.buildCommandField.text)
    }

    @Test
    fun `should reset mainframer path field`() {
        val settingsPanel = setupPanel(mainframerPath = "path")
        settingsPanel.mainframerToolField.text = "unknown location"
        settingsPanel.reset()

        assertEquals("path", settingsPanel.mainframerToolField.text)
    }

    @Test
    fun `should reset remote machine name`() {
        val settingsPanel = setupPanel(remoteMachineName = "remoteName")
        settingsPanel.remoteMachineField.text = "local"
        settingsPanel.reset()

        assertEquals("remoteName", settingsPanel.remoteMachineField.text)
    }

    @Test
    fun `should be modified after changing build command`() {
        val settingsPanel = setupPanel(buildCommand = "example")
        settingsPanel.buildCommandField.text = "other"
        assertTrue(settingsPanel.isModified)
    }

    @Test
    fun `should be modified after changing remote machine name`() {
        val settingsPanel = setupPanel(remoteMachineName = "remote")
        settingsPanel.remoteMachineField.text = "local"
        assertTrue(settingsPanel.isModified)
    }

    @Test
    fun `should be modified after changing mainframer path`() {
        val settingsPanel = setupPanel(mainframerPath = "path")
        settingsPanel.mainframerToolField.text = "location"
        assertTrue(settingsPanel.isModified)
    }

    @JvmField @Rule
    val rule = TemporaryFolder()

    private val toolFile by lazy {
        rule.newFile("mainframer.sh").apply {
            setExecutable(true)
        }
    }

    @Test
    fun `should save build command when configuration applied`() {
        val settingsProvider = createSettingsProvider(mainframerPath = toolFile.absolutePath, buildCommand = "example")
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals("example", settingsProvider.taskData.buildCommand)
    }

    @Test
    fun `should save mainframer path when configuration applied`() {
        val settingsProvider = createSettingsProvider(mainframerPath = toolFile.absolutePath)
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals(toolFile.absolutePath, settingsProvider.taskData.mainframerPath)
    }

    @Test
    fun `should save remote machine name when configuration applied`() {
        val settingsProvider = createSettingsProvider(mainframerPath = toolFile.absolutePath, remoteMachineName = "remote")
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        verify(toolConfiguration).writeRemoteMachineName("remote")
    }

    private fun setupPanel(mainframerPath: String = "a",
                           buildCommand: String = "b",
                           remoteMachineName: String = "remoteName") =
            createPanel(createSettingsProvider(
                    mainframerPath = mainframerPath,
                    buildCommand = buildCommand,
                    remoteMachineName = remoteMachineName))

    private fun createSettingsProvider(mainframerPath: String = "a",
                                       buildCommand: String = "b",
                                       remoteMachineName: String = "remoteName"): MainframerTaskDefaultSettingsProvider {
        return MainframerTaskDefaultSettingsProvider().apply {
            taskData = TaskData(buildCommand = buildCommand, mainframerPath = mainframerPath)
            whenever(toolConfiguration.readRemoteMachineName()).thenReturn(remoteMachineName)
        }
    }

    private fun createPanel(settingsProvider: MainframerTaskDefaultSettingsProvider): TaskDefaultSettingsPanel {
        return TaskDefaultSettingsPanel(mock(), settingsProvider, toolConfiguration).injectUiComponents()
    }
}

private fun TaskDefaultSettingsPanel.injectUiComponents(): TaskDefaultSettingsPanel =
        apply {
            buildCommandField = JTextField()
            remoteMachineField = JTextField()
            mainframerToolField = TextFieldWithBrowseButton()
            reset()
        }
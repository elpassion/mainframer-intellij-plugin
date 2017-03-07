package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.common.assertThrows
import com.elpassion.intelijidea.task.ui.MFBeforeTaskDefaultSettingsPanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import javax.swing.JCheckBox
import javax.swing.JTextField

class MFBeforeTaskDefaultSettingsPanelTest {

    @Test
    fun `should throw configuration exception when build command is empty`() {
        val settingsPanel = setupPanel(buildCommand = "")
        assertThrows<ConfigurationException> {
            settingsPanel.apply()
        }
    }

    @Test
    fun `should throw configuration exception when task name is empty`() {
        val settingsPanel = setupPanel(taskName = "")
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
    fun `should reset task name field`() {
        val settingsPanel = setupPanel(taskName = "some task")
        settingsPanel.taskNameField.text = "other task"
        settingsPanel.reset()

        assertEquals("some task", settingsPanel.taskNameField.text)
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
    fun `should reset configuration on startup field`() {
        val settingsPanel = setupPanel(configureOnStartup = false)
        settingsPanel.configureBeforeTasksOnStartupField.isSelected = true
        settingsPanel.reset()

        assertFalse(settingsPanel.configureBeforeTasksOnStartupField.isSelected)
    }

    @Test
    fun `should be modified after changing build command`() {
        val settingsPanel = setupPanel(buildCommand = "example")
        settingsPanel.buildCommandField.text = "other"
        assertTrue(settingsPanel.isModified)
    }

    @Test
    fun `should be modified after changing task name`() {
        val settingsPanel = setupPanel(taskName = "task1")
        settingsPanel.taskNameField.text = "task2"
        assertTrue(settingsPanel.isModified)
    }

    @Test
    fun `should be modified after changing configureOnStartup field`() {
        val settingsPanel = setupPanel(configureOnStartup = true)
        settingsPanel.configureBeforeTasksOnStartupField.isSelected = false
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

    val mfFile by lazy {
        rule.newFile("mainframer.sh").apply {
            setExecutable(true)
        }
    }

    @Test
    fun `should save build command when configuration applied`() {
        val settingsProvider = createSettingsProvider(buildCommand = "example", mainframerPath = mfFile.absolutePath)
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals("example", settingsProvider.taskData.buildCommand)
    }

    @Test
    fun `should save task name when configuration applied`() {
        val settingsProvider = createSettingsProvider(taskName = "taskName", mainframerPath = mfFile.absolutePath)
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals("taskName", settingsProvider.taskData.taskName)
    }

    @Test
    fun `should save mainframer path when configuration applied`() {
        val settingsProvider = createSettingsProvider(mainframerPath = mfFile.absolutePath)
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals(mfFile.absolutePath, settingsProvider.taskData.mainframerPath)
    }

    @Test
    fun `should save remote machine name when configuration applied`() {
        val settingsProvider = createSettingsProvider(remoteMachineName = "remote", mainframerPath = mfFile.absolutePath)
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals("remote", settingsProvider.state.remoteMachineName)
    }

    @Test
    fun `should save configuration on startup when configuration applied`() {
        val settingsProvider = createSettingsProvider(configureOnStartup = true, mainframerPath = mfFile.absolutePath)
        val settingsPanel = createPanel(settingsProvider)

        settingsPanel.apply()
        assertEquals(true, settingsProvider.state.configureBeforeTaskOnStartup)
    }
}

private fun setupPanel(mainframerPath: String = "a",
                       buildCommand: String = "b",
                       taskName: String = "c",
                       remoteMachineName: String = "remoteName",
                       configureOnStartup: Boolean = false) =
        createPanel(createSettingsProvider(
                mainframerPath = mainframerPath,
                buildCommand = buildCommand,
                taskName = taskName,
                remoteMachineName = remoteMachineName,
                configureOnStartup = configureOnStartup))

private fun createSettingsProvider(mainframerPath: String = "a",
                                   buildCommand: String = "b",
                                   taskName: String = "c",
                                   remoteMachineName: String = "remoteName",
                                   configureOnStartup: Boolean = false): MFBeforeTaskDefaultSettingsProvider {
    return MFBeforeTaskDefaultSettingsProvider().apply {
        taskData = MFTaskData(mainframerPath = mainframerPath, buildCommand = buildCommand, taskName = taskName)
        state.remoteMachineName = remoteMachineName
        state.configureBeforeTaskOnStartup = configureOnStartup
    }
}

private fun createPanel(settingsProvider: MFBeforeTaskDefaultSettingsProvider): MFBeforeTaskDefaultSettingsPanel {
    return MFBeforeTaskDefaultSettingsPanel(mock(), settingsProvider).injectUiComponents()
}

private fun MFBeforeTaskDefaultSettingsPanel.injectUiComponents(): MFBeforeTaskDefaultSettingsPanel =
        apply {
            buildCommandField = JTextField()
            taskNameField = JTextField()
            configureBeforeTasksOnStartupField = JCheckBox()
            remoteMachineField = JTextField()
            mainframerToolField = TextFieldWithBrowseButton()
            reset()
        }
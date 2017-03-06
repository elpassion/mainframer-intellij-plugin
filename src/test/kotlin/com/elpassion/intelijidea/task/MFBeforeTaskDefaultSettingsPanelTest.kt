package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.task.ui.MFBeforeTaskDefaultSettingsPanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.nhaarman.mockito_kotlin.mock
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport
import org.junit.rules.TemporaryFolder
import javax.swing.JCheckBox
import javax.swing.JTextField

@DisplayName("Before task default settings panel test")
class MFBeforeTaskDefaultSettingsPanelTest {

    @Nested
    inner class `should throw configuration exception` {
        @Test
        fun `when build command is empty`() {
            val settingsPanel = setupPanel(buildCommand = "")
            assertThrows<ConfigurationException>(ConfigurationException::class.java) {
                settingsPanel.apply()
            }
        }

        @Test
        fun `when task name is empty`() {
            val settingsPanel = setupPanel(taskName = "")
            assertThrows<ConfigurationException>(ConfigurationException::class.java) {
                settingsPanel.apply()
            }
        }

        @Test
        fun `when mainframer path is not valid`() {
            val settingsPanel = setupPanel(mainframerPath = "")
            assertThrows<ConfigurationException>(ConfigurationException::class.java) {
                settingsPanel.apply()
            }
        }

        @Test
        fun `when remote machine name is empty`() {
            val settingsPanel = setupPanel(remoteMachineName = "")
            assertThrows<ConfigurationException>(ConfigurationException::class.java) {
                settingsPanel.apply()
            }
        }
    }

    @Nested
    inner class `should reset` {
        @Test
        fun `build command field`() {
            val settingsPanel = setupPanel(buildCommand = "build command")
            settingsPanel.buildCommandField.text = "other command"
            settingsPanel.reset()

            assertEquals("build command", settingsPanel.buildCommandField.text)
        }

        @Test
        fun `task name field`() {
            val settingsPanel = setupPanel(taskName = "some task")
            settingsPanel.taskNameField.text = "other task"
            settingsPanel.reset()

            assertEquals("some task", settingsPanel.taskNameField.text)
        }

        @Test
        fun `mainframer path field`() {
            val settingsPanel = setupPanel(mainframerPath = "path")
            settingsPanel.mainframerToolField.text = "unknown location"
            settingsPanel.reset()

            assertEquals("path", settingsPanel.mainframerToolField.text)
        }

        @Test
        fun `remote machine name`() {
            val settingsPanel = setupPanel(remoteMachineName = "remoteName")
            settingsPanel.remoteMachineField.text = "local"
            settingsPanel.reset()

            assertEquals("remoteName", settingsPanel.remoteMachineField.text)
        }

        @Test
        fun `configuration on startup field`() {
            val settingsPanel = setupPanel(configureOnStartup = false)
            settingsPanel.configureBeforeTasksOnStartupField.isSelected = true
            settingsPanel.reset()

            assertFalse(settingsPanel.configureBeforeTasksOnStartupField.isSelected)
        }
    }

    @Nested
    inner class `should be modified` {
        @Test
        fun `after changing build command`() {
            val settingsPanel = setupPanel(buildCommand = "example")
            settingsPanel.buildCommandField.text = "other"
            assertTrue(settingsPanel.isModified)
        }

        @Test
        fun `after changing task name`() {
            val settingsPanel = setupPanel(taskName = "task1")
            settingsPanel.taskNameField.text = "task2"
            assertTrue(settingsPanel.isModified)
        }

        @Test
        fun `after changing configureOnStartup field`() {
            val settingsPanel = setupPanel(configureOnStartup = true)
            settingsPanel.configureBeforeTasksOnStartupField.isSelected = false
            assertTrue(settingsPanel.isModified)
        }

        @Test
        fun `after changing remote machine name`() {
            val settingsPanel = setupPanel(remoteMachineName = "remote")
            settingsPanel.remoteMachineField.text = "local"
            assertTrue(settingsPanel.isModified)
        }

        @Test
        fun `after changing mainframer path`() {
            val settingsPanel = setupPanel(mainframerPath = "path")
            settingsPanel.mainframerToolField.text = "location"
            assertTrue(settingsPanel.isModified)
        }
    }

    @Nested
    @EnableRuleMigrationSupport
    inner class `should save` {

        @JvmField @Rule
        val rule = TemporaryFolder()

        val mfFile by lazy {
            rule.newFile("mainframer.sh").apply {
                setExecutable(true)
            }
        }

        @Test
        fun `build command when configuration applied`() {
            val settingsProvider = createSettingsProvider(buildCommand = "example", mainframerPath = mfFile.absolutePath)
            val settingsPanel = createPanel(settingsProvider)

            settingsPanel.apply()
            assertEquals("example", settingsProvider.taskData.buildCommand)
        }

        @Test
        fun `task name when configuration applied`() {
            val settingsProvider = createSettingsProvider(taskName = "taskName", mainframerPath = mfFile.absolutePath)
            val settingsPanel = createPanel(settingsProvider)

            settingsPanel.apply()
            assertEquals("taskName", settingsProvider.taskData.taskName)
        }

        @Test
        fun `mainframer path when configuration applied`() {
            val settingsProvider = createSettingsProvider(mainframerPath = mfFile.absolutePath)
            val settingsPanel = createPanel(settingsProvider)

            settingsPanel.apply()
            assertEquals(mfFile.absoluteFile, settingsProvider.taskData.mainframerPath)
        }

        @Test
        fun `remote machine name when configuration applied`() {
            val settingsProvider = createSettingsProvider(remoteMachineName = "remote", mainframerPath = mfFile.absolutePath)
            val settingsPanel = createPanel(settingsProvider)

            settingsPanel.apply()
            assertEquals("remote", settingsProvider.state.remoteMachineName)
        }

        @Test
        fun `configuration on startup when configuration applied`() {
            val settingsProvider = createSettingsProvider(configureOnStartup = true, mainframerPath = mfFile.absolutePath)
            val settingsPanel = createPanel(settingsProvider)

            settingsPanel.apply()
            assertEquals(true, settingsProvider.state.configureBeforeTaskOnStartup)
        }
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
package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat

class MainframerSettingsEditorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val taskData = TaskData("newBuildCommand", "newPath")

    override fun setUp() {
        super.setUp()
        MainframerTaskDefaultSettingsProvider.getInstance(project).taskData = taskData
    }

    fun testShouldCallValidateMethodOnResetEditorForm() {
        val runConfiguration = createMainframerRunConfiguration(TaskData("", "path"))
        MainframerSettingsEditor(project).resetFrom(runConfiguration)
        assertThat(runConfiguration.data).isEqualTo(taskData)
    }

    private fun createMainframerRunConfiguration(taskData: TaskData) =
            MainframerRunConfiguration(project, MainframerConfigurationFactory(MainframerConfigurationType()), "").apply {
                data = taskData
            }
}
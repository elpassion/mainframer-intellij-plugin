package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions

class MainframerRunConfigurationDefaultValuesTest : LightPlatformCodeInsightFixtureTestCase() {

    private val taskData = TaskData("newBuildCommand", "newPath")

    override fun setUp() {
        super.setUp()
        MainframerTaskDefaultSettingsProvider.getInstance(project).taskData = taskData
    }

    fun testShouldSetTaskDataToNewOneOnValidateIfBuildCommandIsEmpty() {
        val runConfiguration = createMainframerRunConfiguration(TaskData("", "path"))
        runConfiguration.validate()
        Assertions.assertThat(runConfiguration.data).isEqualTo(taskData)
    }

    fun testShouldSetTaskDataToNewOneOnValidateIfPathIsEmpty() {
        val runConfiguration = createMainframerRunConfiguration(TaskData("buildCommand", ""))
        runConfiguration.validate()
        Assertions.assertThat(runConfiguration.data).isEqualTo(taskData)
    }

    fun testShouldNotSetTaskDataToNewOneOnValidateIfBuildCommandAndPathAreValid() {
        val runConfiguration = createMainframerRunConfiguration(TaskData("buildCommand", "path"))
        runConfiguration.validate()
        Assertions.assertThat(runConfiguration.data).isNotEqualTo(taskData)
    }

    private fun createMainframerRunConfiguration(taskData: TaskData) =
            MainframerRunConfiguration(project, MainframerConfigurationFactory(MainframerConfigurationType()), "").apply {
                data = taskData
            }
}
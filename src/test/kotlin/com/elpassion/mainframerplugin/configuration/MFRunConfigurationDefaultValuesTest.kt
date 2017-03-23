package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions

class MFRunConfigurationDefaultValuesTest : LightPlatformCodeInsightFixtureTestCase() {


    fun testShouldSetMfTaskDataToNewOneOnValidateIfBuildCommandIsEmpty() {
        val mfTaskData = MFTaskData("newBuildCommand", "newPath")
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = mfTaskData
        val mfRunConfiguration = MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
            data = MFTaskData("", "path")
        }
        mfRunConfiguration.validate()
        Assertions.assertThat(mfRunConfiguration.data).isEqualTo(mfTaskData)
    }

    fun testShouldSetMfTaskDataToNewOneOnValidateIfPathIsEmpty() {
        val mfTaskData = MFTaskData("newBuildCommand", "newPath")
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = mfTaskData
        val mfRunConfiguration = MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
            data = MFTaskData("buildCommand", "")
        }
        mfRunConfiguration.validate()
        Assertions.assertThat(mfRunConfiguration.data).isEqualTo(mfTaskData)
    }

    fun testShouldNotSetMfTaskDataToNewOneOnValidateIfBuildCommandAndPathAreValid() {
        val mfTaskData = MFTaskData("newBuildCommand", "newPath")
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = mfTaskData
        val mfRunConfiguration = MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
            data = MFTaskData("buildCommand", "path")
        }
        mfRunConfiguration.validate()
        Assertions.assertThat(mfRunConfiguration.data).isNotEqualTo(mfTaskData)
    }

}
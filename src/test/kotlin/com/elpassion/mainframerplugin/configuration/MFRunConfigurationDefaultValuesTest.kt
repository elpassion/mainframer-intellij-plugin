package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions

class MFRunConfigurationDefaultValuesTest : LightPlatformCodeInsightFixtureTestCase() {

    private val mfTaskData = MFTaskData("newBuildCommand", "newPath")

    override fun setUp() {
        super.setUp()
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = mfTaskData
    }

    fun testShouldSetMfTaskDataToNewOneOnValidateIfBuildCommandIsEmpty() {
        val mfRunConfiguration = createMfRunConfiguration(MFTaskData("", "path"))
        mfRunConfiguration.validate()
        Assertions.assertThat(mfRunConfiguration.data).isEqualTo(mfTaskData)
    }

    fun testShouldSetMfTaskDataToNewOneOnValidateIfPathIsEmpty() {
        val mfRunConfiguration = createMfRunConfiguration(MFTaskData("buildCommand", ""))
        mfRunConfiguration.validate()
        Assertions.assertThat(mfRunConfiguration.data).isEqualTo(mfTaskData)
    }

    fun testShouldNotSetMfTaskDataToNewOneOnValidateIfBuildCommandAndPathAreValid() {
        val mfRunConfiguration = createMfRunConfiguration(MFTaskData("buildCommand", "path"))
        mfRunConfiguration.validate()
        Assertions.assertThat(mfRunConfiguration.data).isNotEqualTo(mfTaskData)
    }

    private fun createMfRunConfiguration(mfTaskData: MFTaskData): MFRunConfiguration {
        return MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
            data = mfTaskData
        }
    }

}
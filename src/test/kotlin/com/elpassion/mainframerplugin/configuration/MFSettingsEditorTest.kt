package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat

class MFSettingsEditorTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldCallValidateMethodOnResetEditorForm() {
        val newMfTaskData = MFTaskData("newBuildCommand", "newPath")
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = newMfTaskData
        val mfRunConfiguration = MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
            data = MFTaskData("", "path")
        }
        MFSettingsEditor(project).resetFrom(mfRunConfiguration)
        assertThat(mfRunConfiguration.data).isEqualTo(newMfTaskData)
    }
}
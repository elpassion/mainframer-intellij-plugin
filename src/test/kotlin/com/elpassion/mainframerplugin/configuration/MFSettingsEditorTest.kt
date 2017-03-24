package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat

class MFSettingsEditorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val newMfTaskData = MFTaskData("newBuildCommand", "newPath")

    override fun setUp() {
        super.setUp()
        MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = newMfTaskData
    }

    fun testShouldCallValidateMethodOnResetEditorForm() {
        val mfRunConfiguration = createMfRunConfiguration(MFTaskData("", "path"))
        MFSettingsEditor(project).resetFrom(mfRunConfiguration)
        assertThat(mfRunConfiguration.data).isEqualTo(newMfTaskData)
    }

    private fun createMfRunConfiguration(mfTaskData: MFTaskData) =
            MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
                data = mfTaskData
            }
}
package com.elpassion.intelijidea

import com.intellij.openapi.options.SettingsEditor
import javax.swing.JComponent


class MFSettingsEditor : SettingsEditor<MFRunnerConfiguration>() {

    private val mainEditorPanel = MFSettingsEditorPanel()

    override fun createEditor(): JComponent {
        mainEditorPanel.taskName.text = MFRunnerConfiguration.DEFAULT_TASK
        return mainEditorPanel.panel
    }

    override fun applyEditorTo(configuration: MFRunnerConfiguration) {
        configuration.taskName = mainEditorPanel.taskName.text
    }

    override fun resetEditorFrom(configuration: MFRunnerConfiguration) {
        configuration.taskName = ""
    }
}
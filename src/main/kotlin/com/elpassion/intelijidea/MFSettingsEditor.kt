package com.elpassion.intelijidea

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import javax.swing.JComponent


class MFSettingsEditor(project: Project) : SettingsEditor<MFRunnerConfiguration>() {

    private val mainEditorPanel = MFSettingsEditorPanel(project)

    override fun createEditor(): JComponent {
        return mainEditorPanel.panel
    }

    override fun applyEditorTo(configuration: MFRunnerConfiguration) {
        configuration.taskName = mainEditorPanel.taskName.text
        configuration.mainframerPath = mainEditorPanel.mainframerScript.text
    }

    override fun resetEditorFrom(configuration: MFRunnerConfiguration) {
        configuration.taskName = ""
        configuration.mainframerPath = ""
    }
}
package com.elpassion.intelijidea

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import java.io.File
import javax.swing.JComponent


class MFSettingsEditor(project: Project) : SettingsEditor<MFRunnerConfiguration>() {

    private val mainEditorPanel = MFSettingsEditorPanel(project)

    override fun createEditor(): JComponent {
        return mainEditorPanel.panel
    }

    override fun applyEditorTo(configuration: MFRunnerConfiguration) {
        configuration.taskName = mainEditorPanel.taskName.text
        configuration.mainframerPath = mainEditorPanel.mainframerScript.text
        if (configuration.mainframerPath.isNullOrEmpty()) {
            throw ConfigurationException("Mainframer path cannot be empty")
        }
        if(File(configuration.mainframerPath).exists().not()){
            throw ConfigurationException("Mainframer path is invalid")
        }
        if (configuration.taskName.isNullOrEmpty()) {
            throw ConfigurationException("Task cannot be empty")
        }
    }

    override fun resetEditorFrom(configuration: MFRunnerConfiguration) {
        configuration.taskName = ""
        configuration.mainframerPath = ""
    }
}
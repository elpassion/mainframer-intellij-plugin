package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.configuration.ui.MFSettingsEditorPanel
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
        configuration.data?.run {
            buildCommand = mainEditorPanel.buildCommand.text
            taskName = mainEditorPanel.taskName.text
            mainframerPath = mainEditorPanel.mainframerScript.text
            if (mainframerPath.isNullOrEmpty()) {
                throw ConfigurationException("Mainframer path cannot be empty")
            }
            if (File(mainframerPath).exists().not()) {
                throw ConfigurationException("Mainframer path is invalid")
            }
            if (taskName.isNullOrEmpty()) {
                throw ConfigurationException("Task cannot be empty")
            }
        }
    }

    override fun resetEditorFrom(configuration: MFRunnerConfiguration) {
        configuration.data.let {
            mainEditorPanel.buildCommand.text = it?.buildCommand ?: "./gradlew"
            mainEditorPanel.taskName.text = it?.taskName ?: "build"
            mainEditorPanel.mainframerScript.text = it?.mainframerPath ?: configuration.project.basePath
        }
    }
}
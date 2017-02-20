package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.configuration.ui.MFSettingsEditorPanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import java.io.File
import javax.swing.JComponent


class MFSettingsEditor(project: Project) : SettingsEditor<MFRunConfiguration>() {

    private val mainEditorPanel = MFSettingsEditorPanel(project)

    override fun createEditor(): JComponent {
        return mainEditorPanel.panel
    }

    override fun applyEditorTo(configuration: MFRunConfiguration) {
        configuration.data = configuration.data?.copy(
                buildCommand = mainEditorPanel.buildCommand.text.trim(),
                taskName = mainEditorPanel.taskName.text.trim(),
                mainframerPath = mainEditorPanel.mainframerTool.text.trim())
        configuration.data?.run {
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

    override fun resetEditorFrom(configuration: MFRunConfiguration) {
        configuration.data.let {
            mainEditorPanel.buildCommand.text = it?.buildCommand
            mainEditorPanel.taskName.text = it?.taskName
            mainEditorPanel.mainframerTool.text = it?.mainframerPath
        }
    }
}
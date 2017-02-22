package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.BuildCommandValidator
import com.elpassion.intelijidea.common.MainframerPathValidator
import com.elpassion.intelijidea.common.TaskFieldValidator
import com.elpassion.intelijidea.common.validateForm
import com.elpassion.intelijidea.configuration.ui.MFSettingsEditorPanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import javax.swing.JComponent


class MFSettingsEditor(project: Project) : SettingsEditor<MFRunConfiguration>() {

    private val mainEditorPanel = MFSettingsEditorPanel(project)

    override fun createEditor(): JComponent {
        return mainEditorPanel.panel
    }

    override fun applyEditorTo(configuration: MFRunConfiguration) {
        configuration.data = updatedConfigurationData(configuration)
        doValidate()
    }

    private fun updatedConfigurationData(configuration: MFRunConfiguration) =
            configuration.data?.copy(
                    buildCommand = mainEditorPanel.buildCommand.text,
                    taskName = mainEditorPanel.taskName.text,
                    mainframerPath = mainEditorPanel.mainframerTool.text)

    private fun doValidate() = with(mainEditorPanel) {
        validateForm(BuildCommandValidator(buildCommand), TaskFieldValidator(taskName), MainframerPathValidator(mainframerTool))?.let { throw ConfigurationException(it.message) }
    }

    override fun resetEditorFrom(configuration: MFRunConfiguration) {
        configuration.data?.let {
            mainEditorPanel.buildCommand.text = it.buildCommand
            mainEditorPanel.taskName.text = it.taskName
            mainEditorPanel.mainframerTool.text = it.mainframerPath ?: ""
        }
    }
}
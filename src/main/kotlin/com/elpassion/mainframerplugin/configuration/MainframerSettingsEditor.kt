package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.BuildCommandValidator
import com.elpassion.mainframerplugin.common.MainframerPathValidator
import com.elpassion.mainframerplugin.common.validateForm
import com.elpassion.mainframerplugin.configuration.ui.SettingsEditorPanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import javax.swing.JComponent


class MainframerSettingsEditor(project: Project) : SettingsEditor<MainframerRunConfiguration>() {

    private val mainEditorPanel = SettingsEditorPanel(project)

    override fun createEditor(): JComponent {
        return mainEditorPanel.panel
    }

    override fun applyEditorTo(configuration: MainframerRunConfiguration) {
        configuration.data = updatedConfigurationData(configuration)
        doValidate()
    }

    private fun updatedConfigurationData(configuration: MainframerRunConfiguration) =
            configuration.data?.copy(
                    buildCommand = mainEditorPanel.buildCommand.text,
                    mainframerPath = mainEditorPanel.mainframerTool.text)

    private fun doValidate() = with(mainEditorPanel) {
        validateForm(BuildCommandValidator(buildCommand), MainframerPathValidator(mainframerTool))?.let { throw ConfigurationException(it.message) }
    }

    override fun resetEditorFrom(configuration: MainframerRunConfiguration) {
        configuration.validate()
        configuration.data?.let {
            mainEditorPanel.buildCommand.text = it.buildCommand
            mainEditorPanel.mainframerTool.text = it.mainframerPath ?: ""
        }
    }
}
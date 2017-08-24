package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.action.configure.configureToolInProject
import com.elpassion.mainframerplugin.action.configure.selector.SelectorResult
import com.elpassion.mainframerplugin.action.configure.selector.selector
import com.elpassion.mainframerplugin.action.configure.selector.showSelectorDialog
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskDefaultOptionsConfigurable
import com.elpassion.mainframerplugin.task.mainframerTaskProvider
import com.elpassion.mainframerplugin.util.showError
import com.elpassion.mainframerplugin.util.showInfo
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import javax.swing.event.HyperlinkEvent

fun selectConfigurationsToMFActions(project: Project) {
    SelectorConfigurationsController(
            manipulateTasks = configureSelections(project),
            selectorResult = selector(project, { configuration, templates ->
                showSelectorDialog(project, configuration, templates)
            }),
            isSettingsTaskValid = project.isTaskSettingValid(),
            showMessage = project.showConfigurationChangesCountInfo(),
            showMainframerNotConfiguredError = project.showConfigureMainframerError(),
            showMainframerTaskInvalidError = project.showInvalidTaskDataError(),
            doesMainframerExists = project.doesMainframerExists())
            .configure()
}

private fun configureSelections(project: Project): (SelectorResult) -> Unit = {
    TaskManipulator(project).run {
        injectToolToConfigurations(project.mainframerTaskProvider, it.toInject)
        restoreConfigurations(it.toRestore)
    }
}

private fun Project.showConfigurationChangesCountInfo() = { injected: Int, restored: Int ->
    showInfo(this, StringsBundle.getMessage("selector.summary", injected, restored))
}

private fun Project.showConfigureMainframerError(): (String) -> Unit = {
    showError(this, it) {
        if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            configureToolInProject(this)
        }
    }
}

private fun Project.showInvalidTaskDataError(): (String) -> Unit = {
    showError(this, it) {
        if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            ShowSettingsUtil.getInstance().showSettingsDialog(this, TaskDefaultOptionsConfigurable::class.java)
        }
    }
}

private fun Project.isTaskSettingValid() = { MainframerTaskDefaultSettingsProvider.getInstance(this).taskData.buildCommand.isNotBlank() }
private fun Project.doesMainframerExists() = { MainframerTaskDefaultSettingsProvider.getInstance(this).taskData.isScriptValid() }

package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.action.configure.configureToolInProject
import com.elpassion.mainframerplugin.action.configure.selector.SelectorResult
import com.elpassion.mainframerplugin.action.configure.selector.selector
import com.elpassion.mainframerplugin.action.configure.selector.showSelectorDialog
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.mainframerTaskProvider
import com.elpassion.mainframerplugin.util.showError
import com.elpassion.mainframerplugin.util.showInfo
import com.intellij.openapi.project.Project
import javax.swing.event.HyperlinkEvent

fun selectConfigurationsToMFActions(project: Project) {
    SelectorConfigurationsController(
            selectorResult = selector(project, { configuration, templates ->
                showSelectorDialog(project, configuration, templates)
            }),
            isSettingsTaskValid = project.isTaskSettingValid(),
            manipulateTasks = configureSelections(project),
            showMessage = project.showConfigurationChangesCountInfo(),
            showError = project.showConfigurationError())
            .configure()
}

private fun configureSelections(project: Project): (SelectorResult) -> Unit = {
    with(it) {
        TaskManipulator(project).run {
            injectToolToConfigurations(project.mainframerTaskProvider, toInject)
            restoreConfigurations(toRestore)
        }
    }
}

private fun Project.showConfigurationChangesCountInfo() = { injected: Int, restored: Int ->
    showInfo(this, StringsBundle.getMessage("selector.summary", injected, restored))
}

private fun Project.showConfigurationError(): (String) -> Unit = {
    showError(this, it) {
        if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            configureToolInProject(this)
        }
    }
}

private fun Project.isTaskSettingValid() = { MainframerTaskDefaultSettingsProvider.getInstance(this).taskData.isValid() }

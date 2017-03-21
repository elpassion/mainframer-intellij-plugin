package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.action.configure.configureMFToProject
import com.elpassion.mainframerplugin.action.configure.selector.MFSelectorResult
import com.elpassion.mainframerplugin.action.configure.selector.mfSelector
import com.elpassion.mainframerplugin.action.configure.selector.showSelectorDialog
import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.mfBeforeRunTaskProvider
import com.elpassion.mainframerplugin.util.showError
import com.elpassion.mainframerplugin.util.showInfo
import com.intellij.openapi.project.Project
import javax.swing.event.HyperlinkEvent

fun selectConfigurationsToMFActions(project: Project) {
    MFSelectorConfigurationsController(
            selectorResult = mfSelector(project, { configuration, templates ->
                showSelectorDialog(project, configuration, templates)
            }),
            isSettingsTaskValid = project.isTaskSettingValid(),
            manipulateTasks = configureSelections(project),
            showMessage = project.showConfigurationChangesCountInfo(),
            showError = project.showMFConfigurationError())
            .configure()
}

private fun configureSelections(project: Project): (MFSelectorResult) -> Unit = {
    with(it) {
        TaskManipulator(project).run {
            injectMFToConfigurations(project.mfBeforeRunTaskProvider, toInject)
            restoreConfigurations(toRestore)
        }
    }
}

private fun Project.showConfigurationChangesCountInfo() = { injected: Int, restored: Int ->
    showInfo(this, "Number of injected configurations $injected, restored to default $restored")
}

private fun Project.showMFConfigurationError(): (String) -> Unit = {
    showError(this, it) {
        if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            configureMFToProject(this)
        }
    }
}

private fun Project.isTaskSettingValid() = { MFBeforeTaskDefaultSettingsProvider.getInstance(this).taskData.isValid() }

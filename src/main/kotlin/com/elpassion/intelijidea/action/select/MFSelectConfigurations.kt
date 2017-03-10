package com.elpassion.intelijidea.action.select

import com.elpassion.intelijidea.action.configure.configureMFToProject
import com.elpassion.intelijidea.action.configure.selector.MFSelectorResult
import com.elpassion.intelijidea.action.configure.selector.mfSelector
import com.elpassion.intelijidea.action.configure.selector.showSelectorDialog
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.elpassion.intelijidea.util.showError
import com.elpassion.intelijidea.util.showInfo
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

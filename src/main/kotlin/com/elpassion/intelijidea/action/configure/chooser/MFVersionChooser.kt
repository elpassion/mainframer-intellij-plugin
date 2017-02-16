package com.elpassion.intelijidea.action.configure.chooser

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.intellij.openapi.project.Project

fun mfVersionChooser(project: Project, provider: MFBeforeTaskDefaultSettingsProvider) = { versionsList: List<String> ->
    MFVersionChooserDialog(project, versionsList)
            .applyDefaultValues(provider.taskData)
            .showAsObservable()
            .doAfterNext { dataFromUi ->
                provider.saveConfiguration(dataFromUi, project.basePath)
            }
            .map { it.version }
}

private fun MFBeforeTaskDefaultSettingsProvider.saveConfiguration(dataFromUi: MFConfiguratorViewModel, mainframerPath: String?) {
    taskData = taskData.copy(
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName,
            mainframerPath = mainframerPath)
}
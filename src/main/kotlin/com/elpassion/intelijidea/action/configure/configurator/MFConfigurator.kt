package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.openapi.project.Project
import io.reactivex.Observable

fun mfVersionChooser(project: Project, provider: MFBeforeTaskDefaultSettingsProvider) = { versionsList: List<String> ->
    showConfigurationDialog(project, versionsList, provider.taskData)
            .doAfterNext { dataFromUi ->
                provider.saveConfiguration(dataFromUi, project.basePath)
            }
            .map { it.version }
}

private fun showConfigurationDialog(project: Project, versionsList: List<String>, defaultValues: MFTaskData): Observable<MFConfiguratorViewModel> =
        Observable.create<MFConfiguratorViewModel> { emitter ->
            MFConfiguratorDialog(project, versionsList, defaultValues, {
                emitter.onNext(it)
                emitter.onComplete()
            }, {
                emitter.onComplete()
            }).show()
        }

private fun MFBeforeTaskDefaultSettingsProvider.saveConfiguration(dataFromUi: MFConfiguratorViewModel, mainframerPath: String?) {
    taskData = taskData.copy(
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName,
            mainframerPath = mainframerPath)
}
package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.openapi.project.Project
import io.reactivex.Observable

fun mfConfigurator(project: Project, provider: MFBeforeTaskDefaultSettingsProvider) = { versionsList: List<String> ->
    showConfigurationDialog(project, versionsList, createDefaultValues(provider.taskData))
            .doAfterNext { dataFromUi ->
                provider.saveConfiguration(dataFromUi, project.basePath)
            }
            .map { it.version }
}

private fun showConfigurationDialog(project: Project, versionsList: List<String>, defaultValues: MFConfiguratorIn): Observable<MFConfiguratorOut> =
        Observable.create<MFConfiguratorOut> { emitter ->
            MFConfiguratorDialog(project, versionsList, defaultValues, {
                emitter.onNext(it)
                emitter.onComplete()
            }, {
                emitter.onComplete()
            }).show()
        }

fun createDefaultValues(taskData: MFTaskData): MFConfiguratorIn {
    return MFConfiguratorIn("", taskName = taskData.taskName, buildCommand = taskData.buildCommand)
}

private fun MFBeforeTaskDefaultSettingsProvider.saveConfiguration(dataFromUi: MFConfiguratorOut, mainframerPath: String?) {
    taskData = taskData.copy(
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName,
            mainframerPath = mainframerPath)
}
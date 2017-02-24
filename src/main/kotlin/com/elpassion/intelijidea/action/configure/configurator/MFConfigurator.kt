package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.common.MFToolConfiguration
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import io.reactivex.Maybe
import java.io.File

fun mfConfigurator(project: Project, configurationFromUi: (MFConfiguratorIn, List<String>) -> Maybe<MFConfiguratorOut>) = { versionList: List<String> ->
    val provider = MFBeforeTaskDefaultSettingsProvider.INSTANCE
    val defaultValues = createDefaultValues(provider.taskData, project.getRemoteMachineName())
    configurationFromUi(defaultValues, listOf("1.0.0"))
            .map { dataFromUi ->
                dataFromUi to createDefaultMfLocation(project)
            }
            .doAfterSuccess { data ->
                val dataFromUi = data.first
                val defaultMfLocation = data.second
                provider.saveConfiguration(createMFTaskData(dataFromUi, defaultMfLocation))
                project.setRemoteMachineName(dataFromUi.remoteName)
            }
            .map { MFToolInfo(it.first.version, it.second) }
}

private fun createDefaultMfLocation(project: Project) = File(project.basePath, mfFilename)

fun showConfigurationDialog(project: Project, versionsList: List<String>, defaultValues: MFConfiguratorIn): Maybe<MFConfiguratorOut> =
        Maybe.create<MFConfiguratorOut> { emitter ->
            MFConfiguratorDialog(project, versionsList, defaultValues, {
                emitter.onSuccess(it)
                emitter.onComplete()
            }, {
                emitter.onComplete()
            }).show()
        }

fun createDefaultValues(taskData: MFTaskData, remoteMachineName: String?): MFConfiguratorIn {
    return MFConfiguratorIn(remoteName = remoteMachineName,
            taskName = taskData.taskName,
            buildCommand = taskData.buildCommand)
}

fun createMFTaskData(dataFromUi: MFConfiguratorOut, file: File): MFTaskData {
    return MFTaskData(mainframerPath = file.absolutePath,
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName)
}


private fun Project.getRemoteMachineName() = ApplicationManager.getApplication().runReadAction<String> {
    MFToolConfiguration(basePath).readRemoteMachineName()
}

private fun Project.setRemoteMachineName(name: String) {
    ApplicationManager.getApplication().runWriteAction {
        MFToolConfiguration(basePath).writeRemoteMachineName(name)
    }
}

private fun MFBeforeTaskDefaultSettingsProvider.saveConfiguration(dataFromUi: MFTaskData) {
    taskData = taskData.copy(
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName,
            mainframerPath = dataFromUi.mainframerPath)
}
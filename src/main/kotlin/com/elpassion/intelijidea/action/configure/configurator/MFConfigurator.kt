package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.common.MFToolConfiguration
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import io.reactivex.Maybe
import java.io.File

fun mfConfigurator(project: Project, configurationFromUi: (MFConfiguratorIn) -> Maybe<MFConfiguratorOut>) = { versionList: List<String> ->
    val provider = MFBeforeTaskDefaultSettingsProvider.INSTANCE
    val defaultValues = createDefaultValues(versionList, provider.taskData, project.getRemoteMachineName())
    configurationFromUi(defaultValues)
            .map { dataFromUi ->
                dataFromUi to createDefaultMfLocation(project)
            }
            .doAfterSuccess { data ->
                provider.saveConfiguration(data)
                project.setRemoteMachineName(data.first.remoteName)
            }
            .map { MFToolInfo(it.first.version, it.second) }
}

private fun createDefaultMfLocation(project: Project) = File(project.basePath, mfFilename)

private fun createDefaultValues(versionList: List<String>, taskData: MFTaskData, remoteMachineName: String?): MFConfiguratorIn {
    return MFConfiguratorIn(versionList = versionList,
            remoteName = remoteMachineName,
            taskName = taskData.taskName,
            buildCommand = taskData.buildCommand)
}

private fun Project.getRemoteMachineName() = ApplicationManager.getApplication().runReadAction<String> {
    MFToolConfiguration(basePath).readRemoteMachineName()
}


private fun Project.setRemoteMachineName(name: String) {
    ApplicationManager.getApplication().runWriteAction {
        MFToolConfiguration(basePath).writeRemoteMachineName(name)
    }
}

private fun MFBeforeTaskDefaultSettingsProvider.saveConfiguration(data: Pair<MFConfiguratorOut, File>) {
    val dataFromUi = createMFTaskData(data.first, data.second)
    taskData = taskData.copy(
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName,
            mainframerPath = dataFromUi.mainframerPath)
}

private fun createMFTaskData(dataFromUi: MFConfiguratorOut, file: File): MFTaskData {
    return MFTaskData(mainframerPath = file.absolutePath,
            buildCommand = dataFromUi.buildCommand,
            taskName = dataFromUi.taskName)
}

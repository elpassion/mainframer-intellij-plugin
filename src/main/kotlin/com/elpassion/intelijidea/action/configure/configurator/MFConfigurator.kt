package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.project.Project
import io.reactivex.Maybe
import java.io.File

fun mfConfigurator(project: Project, configurationFromUi: (MFConfiguratorIn) -> Maybe<MFConfiguratorOut>) = { versionList: List<String> ->
    val mfStorage = MFStorage(project)
    val defaultValues = createDefaultValues(versionList, mfStorage)
    configurationFromUi(defaultValues)
            .map { dataFromUi ->
                dataFromUi to createDefaultMfLocation(project)
            }
            .doAfterSuccess { data ->
                mfStorage.saveConfiguration(data = data.first, file = data.second)
                mfStorage.setRemoteMachineName(data.first.remoteName)
            }
            .map { MFToolInfo(it.first.version, it.second) }
}

private fun createDefaultMfLocation(project: Project) = File(project.basePath, mfFilename)

private fun createDefaultValues(versionList: List<String>, storage: MFStorage): MFConfiguratorIn {
    return MFConfiguratorIn(versionList = versionList,
            remoteName = storage.getRemoteMachineName(),
            taskName = storage.getConfiguration().taskName,
            buildCommand = storage.getConfiguration().buildCommand)
}
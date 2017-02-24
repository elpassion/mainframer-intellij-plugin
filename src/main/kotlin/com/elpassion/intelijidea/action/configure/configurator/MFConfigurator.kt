package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.project.Project
import io.reactivex.Maybe
import java.io.File

fun mfConfigurator(project: Project, configurationFromUi: (MFConfiguratorIn) -> Maybe<MFConfiguratorOut>) = { versionList: List<String> ->
    val mfStorage = MFPluginConfigurationRepository(project)
    val defaultValues = createDefaultValues(versionList, mfStorage.getConfiguration())
    val file = createDefaultMfLocation(project)
    configurationFromUi(defaultValues)
            .doAfterSuccess { data ->
                mfStorage.saveConfiguration(MFPluginConfiguration(data.taskName, data.buildCommand, data.remoteName, file.absolutePath))
            }
            .map { MFToolInfo(it.version, file) }
}

private fun createDefaultMfLocation(project: Project) = File(project.basePath, mfFilename)

private fun createDefaultValues(versionList: List<String>, mfPluginConfiguration: MFPluginConfiguration): MFConfiguratorIn {
    return MFConfiguratorIn(versionList = versionList,
            remoteName = mfPluginConfiguration.remoteName,
            taskName = mfPluginConfiguration.taskName,
            buildCommand = mfPluginConfiguration.buildCommand)
}
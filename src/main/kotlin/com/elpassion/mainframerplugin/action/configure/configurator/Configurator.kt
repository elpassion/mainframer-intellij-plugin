package com.elpassion.mainframerplugin.action.configure.configurator

import com.elpassion.mainframerplugin.util.toolFilename
import com.intellij.openapi.project.Project
import io.reactivex.Maybe
import java.io.File

fun configurator(project: Project, configurationFromUi: (ConfiguratorIn) -> Maybe<ConfiguratorOut>) = { versionList: List<String> ->
    val repository = PluginConfigurationRepository(project)
    val defaultValues = createDefaultValues(versionList, repository.getConfiguration())
    val file = createDefaultToolLocation(project)
    configurationFromUi(defaultValues)
            .doAfterSuccess { (_, remoteName, buildCommand) ->
                repository.saveConfiguration(PluginConfiguration(buildCommand, remoteName, file.absolutePath))
            }
            .map { ToolInfo(it.version, file) }
}

private fun createDefaultToolLocation(project: Project) = File(project.basePath, toolFilename)

private fun createDefaultValues(versionList: List<String>, pluginConfiguration: PluginConfiguration): ConfiguratorIn {
    return ConfiguratorIn(
            versionList = versionList,
            remoteName = pluginConfiguration.remoteName,
            buildCommand = pluginConfiguration.buildCommand)
}

data class ConfiguratorIn(
        val versionList: List<String>,
        val remoteName: String?,
        val buildCommand: String?)

data class ConfiguratorOut(
        val version: String,
        val remoteName: String,
        val buildCommand: String)

data class PluginConfiguration(
        val buildCommand: String,
        val remoteName: String?,
        val mainframerPath: String)
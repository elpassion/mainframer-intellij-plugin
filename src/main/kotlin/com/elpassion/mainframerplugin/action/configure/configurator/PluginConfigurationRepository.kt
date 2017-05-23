package com.elpassion.mainframerplugin.action.configure.configurator

import com.elpassion.mainframerplugin.common.ToolConfigurationImpl
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

class PluginConfigurationRepository(private val project: Project) {

    fun getConfiguration(): PluginConfiguration = with(taskData) {
        PluginConfiguration(buildCommand = buildCommand, remoteName = getRemoteMachineName(), mainframerPath = mainframerPath)
    }

    private fun getRemoteMachineName(): String? {
        return ApplicationManager.getApplication().runReadAction<String> {
            ToolConfigurationImpl(project.basePath).readRemoteMachineName()
        }
    }

    fun saveConfiguration(configuration: PluginConfiguration) {
        saveProviderConfiguration(data = configuration)
        setRemoteMachineName(configuration.remoteName)
    }

    private fun setRemoteMachineName(name: String?) = name?.let {
        ApplicationManager.getApplication().runWriteAction {
            ToolConfigurationImpl(project.basePath).writeRemoteMachineName(name)
        }
    }

    private fun saveProviderConfiguration(data: PluginConfiguration) {
        taskData = taskData.copy(
                buildCommand = data.buildCommand,
                mainframerPath = data.mainframerPath)
    }

    private var taskData: TaskData
        get() = MainframerTaskDefaultSettingsProvider.getInstance(project).taskData
        set(value) {
            MainframerTaskDefaultSettingsProvider.getInstance(project).taskData = value
        }
}
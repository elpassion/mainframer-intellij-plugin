package com.elpassion.mainframerplugin.action.configure.configurator

import com.elpassion.mainframerplugin.common.MFToolConfigurationImpl
import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

class MFPluginConfigurationRepository(private val project: Project) {

    fun getConfiguration(): MFPluginConfiguration = with(taskData) {
        MFPluginConfiguration(buildCommand = buildCommand, remoteName = getRemoteMachineName(), mainframerPath = mainframerPath)
    }

    private fun getRemoteMachineName(): String? {
        return ApplicationManager.getApplication().runReadAction<String> {
            MFToolConfigurationImpl(project.basePath).readRemoteMachineName()
        }
    }

    fun saveConfiguration(configuration: MFPluginConfiguration) {
        saveProviderConfiguration(data = configuration)
        setRemoteMachineName(configuration.remoteName)
    }

    private fun setRemoteMachineName(name: String?) = name?.let {
        ApplicationManager.getApplication().runWriteAction {
            MFToolConfigurationImpl(project.basePath).writeRemoteMachineName(name)
        }
    }

    private fun saveProviderConfiguration(data: MFPluginConfiguration) {
        taskData = taskData.copy(
                buildCommand = data.buildCommand,
                mainframerPath = data.mainframerPath)
    }

    private var taskData: MFTaskData
        get() = MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData
        set(value) {
            MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData = value
        }
}
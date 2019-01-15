package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.task.MainframerTask
import com.elpassion.mainframerplugin.task.MainframerTaskProvider
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManager
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class TaskManipulator(private val project: Project) {
    private val runManager = RunManagerEx.getInstanceEx(project)

    fun injectToolToConfigurations(taskProvider: MainframerTaskProvider, configurations: List<RunConfiguration>) {
        configurations.forEach {
            it.injectMainframerTask(taskProvider)
        }
    }

    private fun RunConfiguration.injectMainframerTask(mainframerTaskProvider: BeforeRunTaskProvider<MainframerTask>) {
        val taskToInject = mainframerTaskProvider.createEnabledTask(this)
        runManager.setBeforeRunTasks(this, listOf(taskToInject), false)
    }

    private fun BeforeRunTaskProvider<MainframerTask>.createEnabledTask(runConfiguration: RunConfiguration) =
            createTask(runConfiguration)!!.apply { isEnabled = true }

    fun restoreConfigurations(configurations: List<RunConfiguration>) {
        configurations.forEach {
            restoreDefaultBeforeRunTasks(it)
        }
    }

    private fun restoreDefaultBeforeRunTasks(configuration: RunConfiguration) {
        val beforeRunTasks = getHardcodedBeforeRunTasks(configuration, project)
        runManager.setBeforeRunTasks(configuration, beforeRunTasks, false)
    }

}

fun RunManager.getTemplateConfigurations(): List<RunConfiguration> {
    val configurationTypes = TemplateConfigurationsProvider.get()
    return configurationTypes.flatMap { it.configurationFactories.toList() }
            .map { getConfigurationTemplate(it) }
            .map { it.configuration }
}

private fun getHardcodedBeforeRunTasks(configuration: RunConfiguration, project: Project): List<BeforeRunTask<*>> {
    val beforeRunProviders = BeforeRunTaskProvider.EXTENSION_POINT_NAME.getExtensions(project)
    return beforeRunProviders.associate { provider -> provider.id to provider.createTask(configuration) }
            .filterValues { task -> task != null && task.isEnabled }
            .map {
                configuration.factory?.configureBeforeRunTaskDefaults(it.key, it.value)
                it.value
            }
            .filterNotNull()
            .filter { it.isEnabled }
}

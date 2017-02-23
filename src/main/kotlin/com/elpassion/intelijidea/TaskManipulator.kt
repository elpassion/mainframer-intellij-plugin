package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.openapi.project.Project

fun injectMainframerBeforeTasks(runManagerEx: RunManagerEx, mfTaskProvider: MFBeforeRunTaskProvider, replaceAll: Boolean) {
    runManagerEx.getConfigurations()
            .filterIsInstance<RunConfigurationBase>()
            .filter { it.isCompileBeforeLaunchAddedByDefault }
            .forEach { configuration ->
                val oldTask = runManagerEx.getFirstMFBeforeRunTask(configuration)
                val taskToInject = when {
                    replaceAll || oldTask == null -> mfTaskProvider.createEnabledTask(configuration)
                    else -> oldTask
                }
                runManagerEx.setBeforeRunTasks(configuration, listOf(taskToInject), false)
            }
}

private fun MFBeforeRunTaskProvider.createEnabledTask(runConfiguration: RunConfigurationBase) = createTask(runConfiguration)
        .apply { isEnabled = true }

fun restoreDefaultBeforeRunTasks(runManager: RunManagerEx, project: Project) {
    runManager.getConfigurations()
            .associate { it to getHardcodedBeforeRunTasks(it, project) }
            .forEach {
                runManager.setBeforeRunTasks(it.key, it.value, false)
            }
}

private fun RunManagerEx.getConfigurations() = allConfigurationsList + getTemplateConfigurations()

private fun RunManagerEx.getFirstMFBeforeRunTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).filterIsInstance<MFBeforeRunTask>().firstOrNull()

private fun getHardcodedBeforeRunTasks(configuration: RunConfiguration, project: Project): List<BeforeRunTask<*>> {
    val beforeRunProviders = BeforeRunTaskProvider.EXTENSION_POINT_NAME.getExtensions(project)
    return beforeRunProviders.associate { provider -> provider.id to provider.createTask(configuration) }
            .filterValues { task -> task != null && task.isEnabled }
            .map {
                configuration.factory.configureBeforeRunTaskDefaults(it.key, it.value)
                it.value
            }
            .filterNotNull()
            .filter { it.isEnabled }
}

private fun RunManagerEx.getTemplateConfigurations(): List<RunConfiguration> {
    val configurationTypes = TemplateConfigurationsProvider.get()
    return configurationTypes.flatMap { it.configurationFactories.toList() }
            .map { getConfigurationTemplate(it) }
            .map { it.configuration }
}

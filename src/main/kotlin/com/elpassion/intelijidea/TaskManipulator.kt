package com.elpassion.intelijidea

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.project.Project
import com.intellij.util.SmartList

fun injectMainframerBeforeTasks(runManagerEx: RunManagerEx, mfTaskProvider: BeforeRunTaskProvider<*>) {
    val existingConfigurations = runManagerEx.getExistingConfigurations()
    val templateConfigurations = runManagerEx.getTemplateConfigurations()
    val configurationTypes = existingConfigurations.values + templateConfigurations.values
    configurationTypes.map { it.configuration }
            .filterIsInstance<RunConfigurationBase>()
            .filter { it.isCompileBeforeLaunchAddedByDefault }
            .forEach {
                val task = mfTaskProvider.createTask(it)
                if (task != null) {
                    task.isEnabled = true
                    runManagerEx.setBeforeRunTasks(it, listOf<BeforeRunTask<*>>(task), false)
                }
            }
}

fun restoreDefaultBeforeRunTasks(runManager: RunManagerEx, project: Project) {
    val configurations = (runManager.getExistingConfigurations() + runManager.getTemplateConfigurations()).values
    configurations.map { it.configuration }
            .associate { it to getHardcodedBeforeRunTasks(it, project) }
            .forEach {
                runManager.setBeforeRunTasks(it.key, it.value, false)
            }
}

private fun getHardcodedBeforeRunTasks(settings: RunConfiguration, myProject: Project): List<BeforeRunTask<*>> {
    val tasks = SmartList<BeforeRunTask<*>>()
    Extensions.getExtensions(BeforeRunTaskProvider.EXTENSION_POINT_NAME, myProject).forEach { provider ->
        val task = provider.createTask(settings)
        if (task != null && task.isEnabled) {
            val providerID = provider.id
            settings.factory.configureBeforeRunTaskDefaults(providerID, task)
            if (task.isEnabled) {
                tasks.add(task)
            }
        }
    }
    return tasks
}

fun RunManagerEx.getExistingConfigurations(): Map<String, RunnerAndConfigurationSettings> = getFieldByReflection("myConfigurations")

fun RunManagerEx.getTemplateConfigurations(): Map<String, RunnerAndConfigurationSettings> = getFieldByReflection("myTemplateConfigurationsMap")

//TODO: remove usage of reflection
private fun <T> Any.getFieldByReflection(fieldName: String): T {
    val declaredField = this.javaClass.getDeclaredField(fieldName)
    declaredField.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    return declaredField.get(this) as T
}

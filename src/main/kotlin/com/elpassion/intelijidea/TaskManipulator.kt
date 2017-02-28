package com.elpassion.intelijidea

import com.elpassion.intelijidea.action.configure.selector.MFSelectorItem
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManager
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.openapi.project.Project

class MFTaskInjector(val project: Project, val mfTaskProvider: MFBeforeRunTaskProvider) {
    val runManager: RunManagerEx = RunManagerEx.getInstanceEx(project)

    fun injectMainframerBeforeTasks(mfConfigurations: List<MFSelectorItem>, replaceAll: Boolean) {
        mfConfigurations
                .forEach { selector ->
                    val configuration = selector.configuration
                    if (selector.isSelected) {
                        injectMFTasks(configuration, replaceAll)
                    } else {
                        restoreDefaultBeforeRunTasks(configuration)
                    }
                }
    }

    private fun injectMFTasks(configuration: RunConfiguration, replaceAll: Boolean) {
        val oldTask = runManager.getFirstMFBeforeRunTask(configuration)
        val taskToInject = when {
            replaceAll || oldTask == null -> mfTaskProvider.createEnabledTask(configuration)
            else -> oldTask
        }
        runManager.setBeforeRunTasks(configuration, listOf(taskToInject), false)
    }

    private fun RunManagerEx.getFirstMFBeforeRunTask(configuration: RunConfiguration) =
            getBeforeRunTasks(configuration).filterIsInstance<MFBeforeRunTask>().firstOrNull()

    private fun MFBeforeRunTaskProvider.createEnabledTask(runConfiguration: RunConfiguration) = createTask(runConfiguration)
            .apply { isEnabled = true }

    private fun restoreDefaultBeforeRunTasks(configuration: RunConfiguration) {
        val beforeRunTasks = getHardcodedBeforeRunTasks(configuration, project)
        runManager.setBeforeRunTasks(configuration, beforeRunTasks, false)
    }

}

@Deprecated("Remove when configuration dialog completed", ReplaceWith("List of MFSelectorItem"))
fun RunManagerEx.getConfigurationsAsSelectorItems(restore: Boolean) =
        (getAllConfigurationsAsSelectorItems(restore) + getTemplateConfigurationsAsSelectorItems(restore))

fun RunManager.getTemplateConfigurations(): List<RunConfiguration> {
    val configurationTypes = TemplateConfigurationsProvider.get()
    return configurationTypes.flatMap { it.configurationFactories.toList() }
            .map { getConfigurationTemplate(it) }
            .map { it.configuration }
}

private fun RunManagerEx.getAllConfigurationsAsSelectorItems(restore: Boolean) =
        allConfigurationsList
                .filterIsInstance<RunConfigurationBase>()
                .filter { it.isCompileBeforeLaunchAddedByDefault }
                .map { MFSelectorItem(it, isTemplate = false, isSelected = restore) }


private fun RunManagerEx.getTemplateConfigurationsAsSelectorItems(restore: Boolean) =
        getTemplateConfigurations()
                .filterIsInstance<RunConfigurationBase>()
                .filter { it.isCompileBeforeLaunchAddedByDefault }
                .map { MFSelectorItem(it, isTemplate = true, isSelected = restore) }

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
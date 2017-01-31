package com.elpassion.intelijidea

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class MFREmoveBeforeTaskAtStartup : StartupActivity {
    override fun runActivity(project: Project) {
        val runManagerEx = RunManagerEx.getInstanceEx(project)
        val mfTaskProvider = BeforeRunTaskProvider.getProvider(project, MFBeforeRunTaskProvider.ID)!!

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

    private fun RunManagerEx.getExistingConfigurations(): Map<String, RunnerAndConfigurationSettings> = getFieldByReflection("myConfigurations")

    private fun RunManagerEx.getTemplateConfigurations(): Map<String, RunnerAndConfigurationSettings> = getFieldByReflection("myTemplateConfigurationsMap")
}

//TODO: remove usage of reflection
private fun <T> Any.getFieldByReflection(fieldName: String): T {
    val declaredField = this.javaClass.getDeclaredField(fieldName)
    declaredField.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    return declaredField.get(this) as T
}

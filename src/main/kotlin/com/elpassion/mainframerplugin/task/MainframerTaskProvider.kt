package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.console.commandLineProvider
import com.elpassion.mainframerplugin.task.edit.TaskEditDialog
import com.elpassion.mainframerplugin.util.MainframerIcons
import com.elpassion.mainframerplugin.util.showError
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import javax.swing.Icon

class MainframerTaskProvider(private val project: Project) : BeforeRunTaskProvider<MainframerTask>() {

    override fun getId(): Key<MainframerTask> = ID

    override fun getDescription(task: MainframerTask): String = TASK_NAME

    override fun getName(): String = TASK_NAME

    override fun getIcon(): Icon = MainframerIcons.mainIcon

    override fun isConfigurable(): Boolean = true

    //TODO switch to Promise implementation
    override fun configureTask(runConfiguration: RunConfiguration, task: MainframerTask): Boolean {
        TaskEditDialog(project).run {
            restoreTaskData(task.data)
            if (showAndGet()) {
                task.data = createTaskDataFromForms()
                return true
            }
        }
        return false
    }

    override fun canExecuteTask(configuration: RunConfiguration, task: MainframerTask): Boolean = task.isValid()

    override fun executeTask(context: DataContext, configuration: RunConfiguration, env: ExecutionEnvironment, task: MainframerTask): Boolean {
        if (!task.isValid()) {
            configuration.project?.showInvalidDataError()
            return false
        }
        return TaskExecutor(project, commandLineProvider).executeSync(task, env.executionId, context)
    }

    override fun createTask(runConfiguration: RunConfiguration): MainframerTask {
        val settingsProvider = MainframerTaskDefaultSettingsProvider.getInstance(project)
        return MainframerTask(settingsProvider.taskData)
    }

    companion object {
        val ID = Key.create<MainframerTask>("MainFrame.BeforeRunTask")
        val TASK_NAME = StringsBundle.getMessage("task.name")
    }

    private fun Project.showInvalidDataError() {
        showError(this, StringsBundle.getMessage("task.settings.invalid.data.error"))
    }

}

val Project.mainframerTaskProvider: MainframerTaskProvider
    get() = BeforeRunTaskProvider.getProvider(this, MainframerTaskProvider.ID) as MainframerTaskProvider
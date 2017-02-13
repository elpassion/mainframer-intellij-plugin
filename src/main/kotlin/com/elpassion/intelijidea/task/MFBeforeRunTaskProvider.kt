package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.task.edit.MFBeforeRunTaskDialog
import com.elpassion.intelijidea.util.showInfo
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import javax.swing.SwingUtilities

class MFBeforeRunTaskProvider(private val project: Project) : BeforeRunTaskProvider<MFBeforeRunTask>() {

    override fun getId(): Key<MFBeforeRunTask> = ID

    override fun getDescription(task: MFBeforeRunTask): String = TASK_NAME

    override fun getName(): String = TASK_NAME

    override fun isConfigurable(): Boolean = true

    override fun configureTask(runConfiguration: RunConfiguration?, task: MFBeforeRunTask): Boolean {
        MFBeforeRunTaskDialog(project).run {
            form.restoreMainframerTask(task)
            if (showAndGet()) {
                task.data = form.createMFTaskDataFromForms()
                return true
            }
        }
        return false
    }

    override fun canExecuteTask(configuration: RunConfiguration?, task: MFBeforeRunTask): Boolean = task.isValid()

    override fun executeTask(context: DataContext, configuration: RunConfiguration?, env: ExecutionEnvironment?, task: MFBeforeRunTask): Boolean {
        SwingUtilities.invokeAndWait {
            configuration?.let {
                showInfo(it.project, "Mainframer is executing task: ${it.name}")
            }
        }
        return MFBeforeRunTaskExecutor(project).executeSync(task)
    }

    override fun createTask(runConfiguration: RunConfiguration?): MFBeforeRunTask? {
        val settingsProvider = MFBeforeTaskDefaultSettingsProvider.INSTANCE
        return MFBeforeRunTask(settingsProvider.taskData)
    }

    companion object {
        val ID = Key.create<MFBeforeRunTask>("MainFrame.BeforeRunTask")
        val TASK_NAME = "MainframerBefore"
    }
}


package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.injectMainframerBeforeTasks
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.elpassion.intelijidea.util.showError
import com.elpassion.intelijidea.util.showInfo
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class MFInjectBeforeRunTasksAction : AnAction(MF_INJECT_BEFORE_RUN_TASK_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { injectBeforeRunTask(it) }
    }

    private fun injectBeforeRunTask(project: Project) {
        val isTaskDataValid = MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData.isValid()
        when (isTaskDataValid) {
            true -> project.showInjectionDialog()
            else -> project.showInvalidSettingsError()
        }
    }

    private fun Project.showInjectionDialog() {
        val result = Messages.showYesNoCancelDialog(this, "MainFramer", "Reset current mainframer task?", null)
        when (result) {
            Messages.YES -> injectMainFramer(true)
            Messages.NO -> injectMainFramer(false)
        }
    }

    private fun Project.injectMainFramer(replaceAll: Boolean) {
        val runManagerEx = RunManagerEx.getInstanceEx(this)
        injectMainframerBeforeTasks(runManagerEx, mfBeforeRunTaskProvider, replaceAll = replaceAll)
        showInfo(this, "Mainframer injected!")
    }

    private fun Project.showInvalidSettingsError() = showError(this, "Mainframer settings is invalid")

    companion object {
        private val MF_INJECT_BEFORE_RUN_TASK_ACTION = "Inject mainframer before run task"
    }
}
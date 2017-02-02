package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.restoreDefaultBeforeRunTasks
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MFRestoreDefaultBeforeRunTasksAction : AnAction(MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION) {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val runManager = RunManagerEx.getInstanceEx(project)
        if (project != null) {
            restoreDefaultBeforeRunTasks(runManager, project)
            Messages.showInfoMessage("BeforeRun tasks default configuration restored.", MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION)
        }
    }

    companion object {
        private val MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION = "Restore default before run tasks"
    }
}
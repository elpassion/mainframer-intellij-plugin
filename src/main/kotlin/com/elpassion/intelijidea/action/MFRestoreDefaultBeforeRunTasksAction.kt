package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.restoreDefaultBeforeRunTasks
import com.elpassion.intelijidea.util.showInfo
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFRestoreDefaultBeforeRunTasksAction : AnAction(MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            val runManager = RunManagerEx.getInstanceEx(it)
            restoreDefaultBeforeRunTasks(runManager, it)
            showInfo(it, "Restored default configuration of before run tasks.")
        }
    }

    companion object {
        private val MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION = "Restore default before run tasks"
    }
}
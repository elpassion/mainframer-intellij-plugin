package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.TaskManipulator
import com.elpassion.intelijidea.getConfigurationsItems
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.elpassion.intelijidea.util.showInfo
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFRestoreDefaultBeforeRunTasksAction : AnAction(MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            TaskManipulator(it, it.mfBeforeRunTaskProvider).run {
                val configurationsToRestore = runManager.getConfigurationsItems()
                restoreConfigurations(configurationsToRestore)
            }
            showInfo(it, "Restored default configuration of before run tasks.")
        }
    }

    companion object {
        private val MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION = "Restore default before run tasks"
    }
}
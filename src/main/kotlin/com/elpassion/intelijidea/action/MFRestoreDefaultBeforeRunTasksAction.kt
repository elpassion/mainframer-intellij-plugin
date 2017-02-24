package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.MFTaskInjector
import com.elpassion.intelijidea.allConfigurationMapMFSelectorItem
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.elpassion.intelijidea.util.showInfo
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFRestoreDefaultBeforeRunTasksAction : AnAction(MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            MFTaskInjector(it, it.mfBeforeRunTaskProvider).let {
                val allConfigurationToInject = it.runManager.allConfigurationMapMFSelectorItem(false)
                it.injectMainframerBeforeTasks(mfConfigurations = allConfigurationToInject, replaceAll = true)
            }
            showInfo(it, "Restored default configuration of before run tasks.")
        }
    }

    companion object {
        private val MF_RESTORE_DEFAULT_BEFORE_RUN_TASK_ACTION = "Restore default before run tasks"
    }
}
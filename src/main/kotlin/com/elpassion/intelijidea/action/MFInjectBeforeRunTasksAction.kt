package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.injectMainframerBeforeTasks
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.elpassion.intelijidea.util.showError
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MFInjectBeforeRunTasksAction : AnAction(MF_INJECT_BEFORE_RUN_TASK_ACTION) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            val runManagerEx = RunManagerEx.getInstanceEx(it)
            val isTaskDataValid = MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData.isValid()
            when (isTaskDataValid) {
                true -> {
                    injectMainframerBeforeTasks(runManagerEx, it.mfBeforeRunTaskProvider)
                    Messages.showInfoMessage("Mainframer injected!", MF_INJECT_BEFORE_RUN_TASK_ACTION)
                }
                else -> showError(it, "Mainframer settings is invalid")
            }
        }
    }

    companion object {
        private val MF_INJECT_BEFORE_RUN_TASK_ACTION = "Inject mainframer before run task"
    }
}
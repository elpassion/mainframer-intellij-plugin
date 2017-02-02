package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.injectMainframerBeforeTasks
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MFInjectBeforeRunTasksAction : AnAction(MF_INJECT_BEFORE_RUN_TASK_ACTION) {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val mfTaskProvider = BeforeRunTaskProvider.getProvider(project, MFBeforeRunTaskProvider.ID)!!
        val runManagerEx = RunManagerEx.getInstanceEx(project)
        injectMainframerBeforeTasks(runManagerEx, mfTaskProvider)
        Messages.showInfoMessage("Mainframer injected!", MF_INJECT_BEFORE_RUN_TASK_ACTION)
    }

    companion object {
        private val MF_INJECT_BEFORE_RUN_TASK_ACTION = "Inject mainframer before run task"
    }
}
package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class MFRemoveBeforeTaskAtStartup : StartupActivity {
    override fun runActivity(project: Project) {
        with(MFBeforeTaskDefaultSettingsProvider.INSTANCE) {
            if (state.configureBeforeTaskOnStartup && taskData.isValid()) {
                injectBeforeRunTask(project)
            }
        }
    }

    private fun injectBeforeRunTask(project: Project) {
        val runManagerEx = RunManagerEx.getInstanceEx(project)
        injectMainframerBeforeTasks(runManagerEx, project.mfBeforeRunTaskProvider)
    }
}
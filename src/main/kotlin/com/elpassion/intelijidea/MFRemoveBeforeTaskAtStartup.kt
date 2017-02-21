package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class MFRemoveBeforeTaskAtStartup : StartupActivity {
    override fun runActivity(project: Project) {
        val settingsProvider = MFBeforeTaskDefaultSettingsProvider.INSTANCE
        if (settingsProvider.taskData.isValid()) {
            injectBeforeRunTask(project, settingsProvider)
        }
    }

    private fun injectBeforeRunTask(project: Project, settingsProvider: MFBeforeTaskDefaultSettingsProvider) {
        if (settingsProvider.state.configureBeforeTaskOnStartup) {
            val runManagerEx = RunManagerEx.getInstanceEx(project)
            injectMainframerBeforeTasks(runManagerEx, project.mfBeforeRunTaskProvider)
        }
    }
}
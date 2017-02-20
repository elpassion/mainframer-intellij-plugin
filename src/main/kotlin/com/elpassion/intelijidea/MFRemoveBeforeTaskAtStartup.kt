package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.intellij.execution.BeforeRunTaskProvider
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
            BeforeRunTaskProvider.getProvider(project, MFBeforeRunTaskProvider.ID)?.let {
                val runManagerEx = RunManagerEx.getInstanceEx(project)
                injectMainframerBeforeTasks(runManagerEx, it)
            }
        }
    }
}
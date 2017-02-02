package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManagerEx
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class MFRemoveBeforeTaskAtStartup : StartupActivity {
    override fun runActivity(project: Project) {
        val settingsProvider = MFBeforeTaskDefaultSettingsProvider.INSTANCE
        if (settingsProvider.getConfigureBeforeTaskOnStartup()) {
            val mfTaskProvider = BeforeRunTaskProvider.getProvider(project, MFBeforeRunTaskProvider.ID)!!
            val runManagerEx = RunManagerEx.getInstanceEx(project)
            injectMainframerBeforeTasks(runManagerEx, mfTaskProvider)
        }
    }
}
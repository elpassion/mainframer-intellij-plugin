package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.common.MFToolConfiguration
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import java.io.File

class MFStorage(private val project: Project) {

    fun getRemoteMachineName(): String? = ApplicationManager.getApplication().runReadAction<String> {
        MFToolConfiguration(project.basePath).readRemoteMachineName()
    }

    fun setRemoteMachineName(name: String) = ApplicationManager.getApplication().runWriteAction {
        MFToolConfiguration(project.basePath).writeRemoteMachineName(name)
    }

    fun getConfiguration(): MFTaskData = MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData

    fun saveConfiguration(data: MFConfiguratorOut, file: File) {
        MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData = MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData.copy(
                buildCommand = data.buildCommand,
                taskName = data.taskName,
                mainframerPath = file.absolutePath)
    }
}
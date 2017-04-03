package com.elpassion.mainframerplugin.common.console

import com.elpassion.mainframerplugin.common.MFStateProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

val mfCommandLineProvider: (Project, MFTaskData) -> GeneralCommandLine = { project, taskData ->
    with(taskData) {
        if (MFStateProvider.getInstance(project).isTurnOn) {
            GeneralCommandLine("bash", mainframerPath, buildCommand)
        } else {
            GeneralCommandLine("bash", "-c", buildCommand)
        }
    }.withWorkDirectory(project.basePath)
}

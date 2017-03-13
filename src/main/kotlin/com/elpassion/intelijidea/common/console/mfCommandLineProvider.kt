package com.elpassion.intelijidea.common.console

import com.elpassion.intelijidea.common.MFStateProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

val mfCommandLineProvider: (Project, MFTaskData) -> GeneralCommandLine = { project, taskData ->
    with(taskData) {
        if (MFStateProvider.getInstance(project).isTurnOn) {
            GeneralCommandLine("bash", mainframerPath, buildCommand, taskName)
        } else {
            GeneralCommandLine("bash", buildCommand, taskName)
        }
    }.withWorkDirectory(project.basePath)
}
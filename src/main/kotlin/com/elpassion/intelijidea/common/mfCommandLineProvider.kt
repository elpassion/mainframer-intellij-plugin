package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

val mfCommandLineProvider: (Project, MFTaskData) -> GeneralCommandLine = { project, taskData ->
    with(taskData) {
        if (MFStateProvider.getInstance(project).isTurnOn) {
            GeneralCommandLine("bash", mainframerPath, buildCommand)
        } else {
            GeneralCommandLine("bash", buildCommand)
        }
    }.withWorkDirectory(project.basePath)
}
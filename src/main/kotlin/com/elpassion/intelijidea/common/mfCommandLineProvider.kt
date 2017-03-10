package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.configuration.MFRunConfigurationData
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

val mfCommandLineProvider2: (Project, MFRunConfigurationData) -> GeneralCommandLine = { project, taskData ->
    with(taskData) {
        if (MFStateProvider.getInstance(project).isTurnOn) {
            GeneralCommandLine("bash", mainframerPath, buildCommand, taskName)
        } else {
            GeneralCommandLine("bash", buildCommand, taskName)
        }
    }.withWorkDirectory(project.basePath)
}
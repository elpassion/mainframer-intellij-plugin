package com.elpassion.mainframerplugin.common.console

import com.elpassion.mainframerplugin.common.StateProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

val commandLineProvider: (Project, TaskData) -> GeneralCommandLine = { project, taskData ->
    with(taskData) {
        if (StateProvider.getInstance(project).isTurnOn) {
            GeneralCommandLine("bash", mainframerPath, buildCommand)
        } else {
            GeneralCommandLine("bash", "-c", buildCommand)
        }
    }.withWorkDirectory(project.basePath)
}

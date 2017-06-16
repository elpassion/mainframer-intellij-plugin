package com.elpassion.mainframerplugin.common.console

import com.elpassion.mainframerplugin.common.StateProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import java.util.stream.Collectors

val commandLineProvider: (Project, TaskData) -> GeneralCommandLine = { project, taskData ->
    with(taskData) {
        if (StateProvider.getInstance(project).isTurnOn) {

            // bash command cannot work with Windows paths so converting to ie c:\ to /mnt/c/ for linux subsystem
            // TODO figure out a way to work with cygwin on windows as well (/cygdrive/c/)
            var modifiedMainframerPath = mainframerPath
            // If windows OS and path starts with Character + Colon, then converting
            if (System.getProperty("os.name").contains("Windows") && mainframerPath.contains("^.:".toRegex())) {
                modifiedMainframerPath = "/mnt/" +
                        // removing the initial colon, changing the drive letter to lower case,
                        // and changing all backslashes to forward slashes
                        mainframerPath.replaceFirst("^.:".toRegex(), mainframerPath[0].toLowerCase().toString())
                                .split("\\\\".toRegex()).stream()
                                .map(String::toString).collect(Collectors.joining("/"))
            }

            GeneralCommandLine("bash", modifiedMainframerPath, buildCommand)
        } else {
            GeneralCommandLine("bash", "-c", buildCommand)
        }
    }.withWorkDirectory(project.basePath)
}

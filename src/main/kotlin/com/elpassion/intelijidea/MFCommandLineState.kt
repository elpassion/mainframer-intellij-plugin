package com.elpassion.intelijidea

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

class MFCommandLineState(private val executionEnvironment: ExecutionEnvironment,
                         private val mainframerPath: String,
                         private val taskName: String) : CommandLineState(executionEnvironment) {
    override fun startProcess(): ProcessHandler {
        return OSProcessHandler(GeneralCommandLine("bash", "$mainframerPath/mainframer.sh", "./gradlew $taskName")
                .withWorkDirectory(executionEnvironment.project.basePath))
    }
}
package com.elpassion.intelijidea

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

class MFCommandLineState(private val executionEnvironment: ExecutionEnvironment,
                         private val taskName: String) : CommandLineState(executionEnvironment) {
    override fun startProcess(): ProcessHandler {
        val project = executionEnvironment.project
        return OSProcessHandler(GeneralCommandLine("./gradlew", taskName).withWorkDirectory(project.basePath))
    }
}
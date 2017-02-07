package com.elpassion.intelijidea.common

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

class MFCommandLineState(private val executionEnvironment: ExecutionEnvironment,
                         private val mainframerPath: String?,
                         private val buildCommand: String,
                         private val taskName: String) : CommandLineState(executionEnvironment) {

    override fun startProcess(): ProcessHandler =
            ColoredProcessHandler(createCommandLine().withWorkDirectory(executionEnvironment.project.basePath))

    private fun createCommandLine() = MFCommandLine(mainframerPath, buildCommand, taskName)
}
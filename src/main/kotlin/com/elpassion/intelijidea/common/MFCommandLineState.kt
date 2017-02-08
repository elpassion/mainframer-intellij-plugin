package com.elpassion.intelijidea.common

import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.util.Key

class MFCommandLineState(private val executionEnvironment: ExecutionEnvironment,
                         private val mainframerPath: String?,
                         private val buildCommand: String,
                         private val taskName: String) : CommandLineState(executionEnvironment) {

    override fun startProcess(): ProcessHandler =
            ColoredProcessHandler(createCommandLine().withWorkDirectory(executionEnvironment.project.basePath))

    private fun createCommandLine() = MFCommandLine(mainframerPath, buildCommand, taskName)

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        return super.execute(executor, runner).apply {
            with(processHandler) {
                addOnTextAvailableListener(onText = { bringConsoleToFront(executor) })
            }
        }
    }

    private fun ProcessHandler.bringConsoleToFront(executor: Executor) {
        ExecutionManager.getInstance(executionEnvironment.project).contentManager.toFrontRunContent(executor, this)
    }
}

private fun ProcessHandler.addOnTextAvailableListener(onText: () -> Unit) {
    addProcessListener(createOnTextAvailableListener(onText))
}

private fun createOnTextAvailableListener(onText: () -> Unit) = object : ProcessAdapter() {
    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        if (outputType === ProcessOutputTypes.STDOUT || outputType === ProcessOutputTypes.STDERR) {
            onText()
        }
    }
}
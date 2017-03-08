package com.elpassion.intelijidea.common

import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner

class MFCommandLineState(private val executionEnvironment: ExecutionEnvironment,
                         private val mainframerPath: String?,
                         private val buildCommand: String,
                         private val taskName: String) : CommandLineState(executionEnvironment) {

    override fun startProcess(): ProcessHandler = ColoredProcessHandler(createCommandLine()).apply {
        ProcessTerminatedListener.attach(this)
    }

    private fun createCommandLine() = createMfCommandLine(mainframerPath, buildCommand, taskName)
            .withWorkDirectory(executionEnvironment.project.basePath)

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        return BringConsoleToFrontExecutionResult(super.execute(executor, runner), environment, executor)
    }
}

class BringConsoleToFrontExecutionResult(
        executionResult: ExecutionResult,
        val environment: ExecutionEnvironment,
        val executor: Executor) : ExecutionResult by executionResult {

    init {
        processHandler.addProcessListener(OnProcessStartedProcessAdapter({ bringConsoleToFront() }))
    }

    private fun bringConsoleToFront() {
        ExecutionManager.getInstance(environment.project).contentManager.toFrontRunContent(executor, processHandler)
    }

    class OnProcessStartedProcessAdapter(val onProcessStarted: () -> Unit) : ProcessAdapter() {
        override fun startNotified(event: ProcessEvent?) {
            onProcessStarted()
        }
    }
}
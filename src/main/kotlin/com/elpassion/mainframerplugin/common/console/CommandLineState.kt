package com.elpassion.mainframerplugin.common.console

import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner

class CommandLineState(executionEnvironment: ExecutionEnvironment,
                       private val commandLine: GeneralCommandLine) : CommandLineState(executionEnvironment) {

    override fun startProcess(): ProcessHandler = ColoredProcessHandler(commandLine).apply {
        ProcessTerminatedListener.attach(this)
    }

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        return BringConsoleToFrontExecutionResult(super.execute(executor, runner), environment, executor)
    }
}

class BringConsoleToFrontExecutionResult(
        executionResult: ExecutionResult,
        val environment: ExecutionEnvironment,
        val executor: Executor) : ExecutionResult by executionResult {

    init {
        processHandler.addProcessListener(OnProcessStartedProcessAdapter { bringConsoleToFront() })
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
package com.elpassion.intelijidea

import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ModuleRunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.Key
import javax.swing.Icon

class MFBeforeRunTaskProfile : ModuleRunProfile {
    private val myCommandLine: GeneralCommandLine = GeneralCommandLine("./gradlew", "assembleDebug")

    override fun getName(): String = "MFBeforeRunTaskProfile"

    override fun getIcon(): Icon? = null

    override fun getModules(): Array<Module> = Module.EMPTY_ARRAY

    override fun getState(executor: Executor, env: ExecutionEnvironment): RunProfileState? {
        val project = env.project
        val commandLineState = object : CommandLineState(env) {
            internal fun createCommandLine(): GeneralCommandLine {
                return myCommandLine
            }

            override fun startProcess(): OSProcessHandler {
                val commandLine = createCommandLine().withWorkDirectory(project.basePath)
                val processHandler = ColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }

            override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
                val result = super.execute(executor, runner)
                val processHandler = result.processHandler
                if (processHandler != null) {
                    processHandler.addProcessListener(MFProcessAdapter(project, name))
                    processHandler.addProcessListener(object : ProcessAdapter() {
                        override fun onTextAvailable(event: ProcessEvent?, outputType: Key<*>?) {
                            if (outputType === ProcessOutputTypes.STDOUT || outputType === ProcessOutputTypes.STDERR) {
                                ExecutionManager.getInstance(project).contentManager.toFrontRunContent(executor, processHandler)
                            }
                        }
                    })
                }
                return result
            }
        }
        val builder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
        commandLineState.consoleBuilder = builder
        return commandLineState
    }
}
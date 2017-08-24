package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.console.CommandLineState
import com.intellij.execution.Executor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ModuleRunProfile
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import javax.swing.Icon

class MainframerRunProfile(private val task: MainframerTask, private val commandLineProvider: (TaskData) -> GeneralCommandLine) : ModuleRunProfile {

    override fun getName(): String = StringsBundle.getMessage("task.profile.name")

    override fun getIcon(): Icon? = null

    override fun getModules(): Array<Module> = Module.EMPTY_ARRAY

    override fun getState(executor: Executor, env: ExecutionEnvironment) = CommandLineState(env, commandLineProvider(task.data))
}
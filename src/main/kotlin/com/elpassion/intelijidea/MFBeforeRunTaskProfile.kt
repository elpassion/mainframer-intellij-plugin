package com.elpassion.intelijidea

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ModuleRunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import javax.swing.Icon

class MFBeforeRunTaskProfile(private val task: MFBeforeRunTask) : ModuleRunProfile {
    override fun getName(): String = "MFBeforeRunTaskProfile"

    override fun getIcon(): Icon? = null

    override fun getModules(): Array<Module> = Module.EMPTY_ARRAY

    override fun getState(executor: Executor, env: ExecutionEnvironment): RunProfileState? {
        return MFCommandLineState(env, mainframerPath = task.mainframerPath, buildCommand = task.buildCommand, taskName = task.taskName)
    }
}
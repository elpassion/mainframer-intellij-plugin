package com.elpassion.intelijidea

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project

class MFRunConfiguration(project: Project, configurationFactory: MFConfigurationFactory, name: String) : RunConfigurationBase(project, configurationFactory, name) {

    override fun getConfigurationEditor() = MFSettingsEditor()

    override fun checkConfiguration() {
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return null
    }

}
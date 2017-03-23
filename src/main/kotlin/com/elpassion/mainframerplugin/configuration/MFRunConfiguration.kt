package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.console.MFCommandLineState
import com.elpassion.mainframerplugin.common.console.mfCommandLineProvider
import com.elpassion.mainframerplugin.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class MFRunConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var data: MFTaskData? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment) = with(data ?: getDefaultData()) {
        createCommandLineState(environment, mfCommandLineProvider(project, this))
    }

    private fun createCommandLineState(environment: ExecutionEnvironment, commandLine: GeneralCommandLine): MFCommandLineState {
        return MFCommandLineState(environment, commandLine).apply {
            consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
        }
    }

    override fun checkConfiguration() = with(data ?: getDefaultData()) {
        if (buildCommand.isBlank()) throw RuntimeConfigurationError("Build command cannot be empty")
        if (!isScriptValid()) throw RuntimeConfigurationError("Mainframer tool cannot be found")
    }

    fun validate() {
        if (data?.buildCommand.isNullOrEmpty() or data?.mainframerPath.isNullOrEmpty()) {
            data = getDefaultData()
        }
    }

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean = false

    private fun getDefaultData() = MFBeforeTaskDefaultSettingsProvider.getInstance(project).taskData

}
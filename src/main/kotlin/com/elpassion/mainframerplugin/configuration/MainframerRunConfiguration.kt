package com.elpassion.mainframerplugin.configuration

import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.console.CommandLineState
import com.elpassion.mainframerplugin.common.console.commandLineProvider
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.elpassion.mainframerplugin.util.fromJson
import com.elpassion.mainframerplugin.util.toJson
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element

class MainframerRunConfiguration(
    project: Project,
    configurationFactory: ConfigurationFactory,
    name: String
) : LocatableConfigurationBase<RunConfigurationOptions>(project, configurationFactory, name) {

    var data: TaskData? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MainframerSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment) = with(data ?: getDefaultData()) {
        createCommandLineState(environment, commandLineProvider(project, this, environment.dataContext))
    }

    private fun createCommandLineState(environment: ExecutionEnvironment, commandLine: GeneralCommandLine): CommandLineState {
        return CommandLineState(environment, commandLine).apply {
            consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project)
        }
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        data = element.getAttributeValue(CONFIGURATION_ATTR_DATA)?.fromJson() ?: getDefaultData()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        data?.let { element.setAttribute(CONFIGURATION_ATTR_DATA, it.toJson()) }
    }

    override fun checkConfiguration() = with(data ?: getDefaultData()) {
        if (buildCommand.isBlank()) throw RuntimeConfigurationError(StringsBundle.getMessage("common.form.validator.build.command.empty"))
        if (!isScriptValid()) throw RuntimeConfigurationError(StringsBundle.getMessage("common.form.validator.script.not.found"))
    }

    fun validate() {
        if (data?.buildCommand.isNullOrEmpty() or data?.mainframerPath.isNullOrEmpty()) {
            data = getDefaultData()
        }
    }

    private fun getDefaultData() = MainframerTaskDefaultSettingsProvider.getInstance(project).taskData

    companion object {
        private const val CONFIGURATION_ATTR_DATA = "MFRun.data"
    }

}

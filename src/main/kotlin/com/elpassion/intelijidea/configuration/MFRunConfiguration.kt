package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.MFCommandLineState
import com.elpassion.intelijidea.common.mfCommandLineProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.elpassion.intelijidea.util.fromJson
import com.elpassion.intelijidea.util.toJson
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element

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
        if (taskName.isBlank()) throw RuntimeConfigurationError("Task name cannot be empty")
        if (!isScriptValid()) throw RuntimeConfigurationError("Mainframer tool cannot be found")
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        data = element.getAttributeValue(CONFIGURATION_ATTR_DATA)?.fromJson<MFTaskData>() ?: getDefaultData()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        data?.let { element.setAttribute(CONFIGURATION_ATTR_DATA, it.toJson()) }
    }

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean = false

    private fun getDefaultData() = MFTaskData(
            buildCommand = "./gradlew",
            taskName = "build",
            mainframerPath = project.basePath!!)

    companion object {
        private val CONFIGURATION_ATTR_DATA = "MFRun.data"
    }
}
package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.MFCommandLineState
import com.elpassion.intelijidea.util.fromJson
import com.elpassion.intelijidea.util.toJson
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element
import java.io.File
import java.io.Serializable

class MFRunConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var data: MFRunConfigurationData? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment) = with(data ?: getDefaultData()) {
        createCommandLineState(environment, this)
    }

    private fun createCommandLineState(environment: ExecutionEnvironment, data: MFRunConfigurationData): MFCommandLineState {
        return MFCommandLineState(environment, data.mainframerPath, data.buildCommand, data.taskName).apply {
            consoleBuilder = TextConsoleBuilderFactory.getInstance()
                    .createBuilder(project)
                    .filters(RemoteToLocalTranslatingFilter(project))
        }
    }

    override fun checkConfiguration() = with(data ?: getDefaultData()) {
        if (buildCommand.isBlank()) throw RuntimeConfigurationError("Build command cannot be empty")
        if (taskName.isBlank()) throw RuntimeConfigurationError("Task name cannot be empty")
        if (!mainframerPath.isMfFileAvailable()) throw RuntimeConfigurationError("Mainframer tool cannot be found")
    }

    private fun String.isMfFileAvailable() = File(this).let {
        it.exists() && it.isFile && it.canExecute()
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        data = element.getAttributeValue(CONFIGURATION_ATTR_DATA)?.fromJson<MFRunConfigurationData>() ?: getDefaultData()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        data?.let { element.setAttribute(CONFIGURATION_ATTR_DATA, it.toJson()) }
    }

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean = false

    private fun getDefaultData() = MFRunConfigurationData(
            buildCommand = "./gradlew",
            taskName = "build",
            mainframerPath = project.basePath!!)

    companion object {
        private val CONFIGURATION_ATTR_DATA = "MFRun.data"
    }
}

data class MFRunConfigurationData(val buildCommand: String = "",
                                  val taskName: String = "",
                                  val mainframerPath: String = "") : Serializable

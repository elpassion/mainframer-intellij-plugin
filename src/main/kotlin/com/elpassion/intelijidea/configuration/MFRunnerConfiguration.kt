package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.MFCommandLineState
import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.*
import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element
import java.io.File
import java.io.Serializable
import javax.swing.event.HyperlinkEvent


class MFRunnerConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var data: MFRunnerConfigurationData? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment) = with(data) {
        when {
            this == null -> throw ExecutionException("Mainframer script cannot be found")
            this.isMfFileAvailable() -> {
                showScriptNotFoundError()
                throw ExecutionException("Mainframer script cannot be found")
            }
            else -> MFCommandLineState(environment, mainframerPath, buildCommand, taskName)
        }
    }

    override fun checkConfiguration() = with(data) {
        when {
            this == null -> throw RuntimeConfigurationError("Configuration incorrect")
            buildCommand.isNullOrBlank() -> throw RuntimeConfigurationError("Build command cannot be empty")
            taskName.isNullOrBlank() -> throw RuntimeConfigurationError("Taskname cannot be empty")
            else -> Unit
        }
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        data = element.getAttributeValue(CONFIGURATION_ATTR_DATA)?.fromJson<MFRunnerConfigurationData>() ?:
                MFRunnerConfigurationData(
                        buildCommand = "./gradlew",
                        taskName = "build",
                        mainframerPath = project.basePath)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        data?.let { element.setAttribute(CONFIGURATION_ATTR_DATA, it.toJson()) }
    }

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean = false

    private fun MFRunnerConfigurationData?.isMfFileAvailable() = this?.mainframerPath?.let { File(it, mfFilename).exists() } ?: false

    private fun showScriptNotFoundError() {
        showError(project, errorMessage) {
            if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                MFDownloader.downloadFileToProject(it.url.toString(), project, mfFilename)
            }
        }
    }

    private val errorMessage: String
        get() = "Cannot find <b>$mfFilename</b> in the following path:\n\"${data?.mainframerPath}\"\n\n" +
                "<a href=\"${getLatestMfToolDownloadUrl()}\">Download latest script file</a>"

    companion object {
        private val CONFIGURATION_ATTR_DATA = "MFRunner.data"
    }
}

data class MFRunnerConfigurationData(val buildCommand: String = "",
                                     val taskName: String = "",
                                     val mainframerPath: String? = null) : Serializable

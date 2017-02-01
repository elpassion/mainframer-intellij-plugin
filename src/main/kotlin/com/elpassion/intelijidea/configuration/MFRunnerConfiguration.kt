package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.MFCommandLineState
import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.mfFilename
import com.elpassion.intelijidea.util.mfScriptDownloadUrl
import com.elpassion.intelijidea.util.showError
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element
import java.io.File
import javax.swing.event.HyperlinkEvent

class MFRunnerConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var buildCommand: String? = null
    var taskName: String? = null
    var mainframerPath: String? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (isMfFileAvailable()) {
            return MFCommandLineState(environment, mainframerPath!!, buildCommand!!, taskName!!)
        } else {
            showScriptNotFoundError()
            return null
        }
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        taskName = element.getAttributeValue(CONFIGURATION_ATTR_TASK_NAME)
        mainframerPath = element.getAttributeValue(CONFIGURATION_ATTR_MAINFRAMER_PATH)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        taskName?.let { element.setAttribute(CONFIGURATION_ATTR_TASK_NAME, it) }
        mainframerPath?.let { element.setAttribute(CONFIGURATION_ATTR_MAINFRAMER_PATH, it) }
    }

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean = false

    fun isValid(): Boolean = isMfFileAvailable() && !taskName.isNullOrEmpty()

    private fun isMfFileAvailable() = mainframerPath?.let { File(it, mfFilename).exists() } ?: false

    private fun showScriptNotFoundError() {
        showError(project, "Cannot find <b>$mfFilename</b> in the following path:\n\"$mainframerPath\"\n\n" +
                "<a href=\"$mfScriptDownloadUrl\">Download latest script file</a>") {
            if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                MFDownloader.downloadFileToProject(it.url.toString(), project, mfFilename)
            }
        }
    }

    companion object {
        private val CONFIGURATION_ATTR_TASK_NAME = "MFRunner.taskName"
        private val CONFIGURATION_ATTR_MAINFRAMER_PATH = "MFRunner.mainframerPath"
    }
}
package com.elpassion.intelijidea.configuration

import com.elpassion.intelijidea.common.MFCommandLineState
import com.elpassion.intelijidea.configuration.MFSettingsEditor
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
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import org.jdom.Element
import java.io.File
import java.net.URL
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
        if (isMainframerScriptAvailable()) {
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

    fun isValid(): Boolean = isMainframerScriptAvailable() && !taskName.isNullOrEmpty()

    private fun isMainframerScriptAvailable() = mainframerPath?.let { File(it, mfFilename).exists() } ?: false

    private fun showScriptNotFoundError() {
        showError(project, "Cannot find <b>$mfFilename</b> in the following path:\n\"$mainframerPath\"\n\n" +
                "<a href=\"$mfScriptDownloadUrl\">Download latest script file</a>") {
            if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) downloadFile(it.url)
        }
    }

    private fun downloadFile(url: URL) {
        DownloadUtil.provideDataWithProgressSynchronously(project, "Downloading file",
                "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}...", {
            val progressIndicator = ProgressManager.getInstance().progressIndicator
            DownloadUtil.downloadAtomically(progressIndicator, url.toString(), File(project.basePath, mfFilename))
            project.baseDir.refresh(true, false)
        }, null)
    }

    companion object {
        private val CONFIGURATION_ATTR_TASK_NAME = "MFRunner.taskName"
        private val CONFIGURATION_ATTR_MAINFRAMER_PATH = "MFRunner.mainframerPath"
    }
}
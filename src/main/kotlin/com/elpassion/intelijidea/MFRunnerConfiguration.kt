package com.elpassion.intelijidea

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
import com.intellij.platform.templates.github.DownloadUtil
import org.jdom.Element
import java.io.File
import java.net.URL
import kotlin.concurrent.thread

class MFRunnerConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var taskName: String? = null
    var mainframerPath: String? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor(project)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (isMainframerScriptAvailable() && taskName != null) {
            return MFCommandLineState(environment, mainframerPath!!, taskName!!)
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

    private fun isMainframerScriptAvailable() = mainframerPath?.let { File(it, "mainframer.sh").exists() } ?: false

    private fun showScriptNotFoundError() {
        showError(project, "Couldn't find mainframer.sh file in path: $mainframerPath\n" +
                "<a href=\"$mfScriptDownloadUrl\">Click to download latest script</a>") { downloadFile(it.url) }
    }

    private fun downloadFile(url: URL) {
        thread {
            DownloadUtil.downloadAtomically(null, url.toString(), File(project.basePath, "mainframer.sh"))
        }
    }

    companion object {
        private val CONFIGURATION_ATTR_TASK_NAME = "MFRunner.taskName"
        private val CONFIGURATION_ATTR_MAINFRAMER_PATH = "MFRunner.mainframerPath"
    }
}
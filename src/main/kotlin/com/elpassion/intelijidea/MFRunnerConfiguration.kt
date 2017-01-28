package com.elpassion.intelijidea

import com.elpassion.intelijidea.util.showBalloon
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
            showBalloon(project, "Couldn't find mainframer.sh file in project base directory.")
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
        element.setAttribute(CONFIGURATION_ATTR_TASK_NAME, taskName)
        element.setAttribute(CONFIGURATION_ATTR_MAINFRAMER_PATH, mainframerPath)
    }

    private fun isMainframerScriptAvailable() = mainframerPath != null && File(mainframerPath).exists()

    override fun isCompileBeforeLaunchAddedByDefault(): Boolean = false

    fun isValid(): Boolean = File(mainframerPath).exists() && !taskName.isNullOrEmpty()

    companion object {
        private val CONFIGURATION_ATTR_TASK_NAME = "MFRunner.taskName"
        private val CONFIGURATION_ATTR_MAINFRAMER_PATH = "MFRunner.taskName"
    }
}
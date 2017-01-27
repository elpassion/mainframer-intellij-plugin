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

class MFRunnerConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var taskName: String = DEFAULT_TASK

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (isMainframerScriptAvailable()) {
            return MFCommandLineState(environment, taskName)
        } else {
            showBalloon(project, "Couldn't find mainframer.sh file in project base directory.")
            return null
        }
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        taskName = element.getAttributeValue(CONFIGURATION_ATTR_TASK_NAME)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute(CONFIGURATION_ATTR_TASK_NAME, taskName)
    }

    private fun isMainframerScriptAvailable() = project.baseDir.findChild("mainframer.sh") != null

    companion object {
        val DEFAULT_TASK = "assembleDebug"
        private val CONFIGURATION_ATTR_TASK_NAME = "MFRunner.taskName"
    }
}
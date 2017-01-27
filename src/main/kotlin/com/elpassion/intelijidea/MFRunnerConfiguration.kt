package com.elpassion.intelijidea

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element

class MFRunnerConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String)
    : LocatableConfigurationBase(project, configurationFactory, name) {

    var taskName: String = DEFAULT_TASK

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MFSettingsEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return MFCommandLineState(environment, taskName)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(taskName, element)
    }

    override fun writeExternal(element: Element?) {
        super.writeExternal(element)
        XmlSerializer.serializeInto(taskName, element)
    }

    companion object{
        val DEFAULT_TASK = "assembleDebug"
    }
}
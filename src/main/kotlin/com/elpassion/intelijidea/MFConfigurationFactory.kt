package com.elpassion.intelijidea

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.project.Project

class MFConfigurationFactory(configurationType: ConfigurationType) : ConfigurationFactory(configurationType) {

    override fun createTemplateConfiguration(project: Project) = MFRunnerConfiguration(project, this, "Mainframer")

    override fun getName() = FACTORY_NAME

    companion object {
        private val FACTORY_NAME = "Factory for mainframer configuration"
    }
}

package com.elpassion.mainframerplugin.action.select

import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationType.CONFIGURATION_TYPE_EP

object TemplateConfigurationsProvider {
    var testValue: List<ConfigurationType>? = null

    fun get() = testValue ?: CONFIGURATION_TYPE_EP.extensions.toList()
}
package com.elpassion.intelijidea

import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.extensions.Extensions

object TemplateConfigurationsProvider {
    var testValue : List<ConfigurationType>? = null

    fun get() = testValue ?: Extensions.getExtensions(ConfigurationType.CONFIGURATION_TYPE_EP, null).toList()
}
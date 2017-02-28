package com.elpassion.intelijidea.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration

data class MFSelectorItem(val configuration: RunConfiguration, val isSelected: Boolean) {

    val type: String
        get() = configuration.type.displayName

    val isTemplate: Boolean
        get() = configuration.name.isBlank()

    fun getName(): String = if (isTemplate) {
        "$type template"
    } else {
        "[$type] ${configuration.name}"
    }
}
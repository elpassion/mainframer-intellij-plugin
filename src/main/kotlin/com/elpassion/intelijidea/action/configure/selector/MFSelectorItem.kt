package com.elpassion.intelijidea.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration

data class MFSelectorItem(val configuration: RunConfiguration, val isTemplate: Boolean, val isSelected: Boolean) {

    val type: String
        get() = configuration.type.displayName

    fun getName(): String = if (isTemplate) type else "[$type] ${configuration.name}"
}
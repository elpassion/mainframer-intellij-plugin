package com.elpassion.intelijidea.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration

data class MFSelectorItem(val configuration: RunConfiguration, val isSelected: Boolean) {

    fun getName(): String = configuration.toString()
}
package com.elpassion.mainframerplugin.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration

data class MFSelectorItem(val configuration: RunConfiguration, val isSelected: Boolean, val name: String)

package com.elpassion.mainframerplugin.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration

data class MFSelectorResult(val toInject: List<RunConfiguration>,
                            val toRestore: List<RunConfiguration>)
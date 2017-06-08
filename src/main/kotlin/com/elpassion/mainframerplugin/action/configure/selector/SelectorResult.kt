package com.elpassion.mainframerplugin.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration

data class SelectorResult(val toInject: List<RunConfiguration>,
                          val toRestore: List<RunConfiguration>)
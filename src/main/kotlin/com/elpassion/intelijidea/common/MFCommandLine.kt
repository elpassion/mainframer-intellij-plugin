package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.execution.configurations.GeneralCommandLine

class MFCommandLine(val buildCommand: String, val taskName: String) : GeneralCommandLine() {

    init {
        exePath = "bash"
        addParameters("$mfFilename $buildCommand $taskName")
    }
}
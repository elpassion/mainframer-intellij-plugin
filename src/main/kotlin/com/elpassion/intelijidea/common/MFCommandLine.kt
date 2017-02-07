package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.execution.configurations.GeneralCommandLine

class MFCommandLine(val mfPath: String? = null, val buildCommand: String, val taskName: String) : GeneralCommandLine() {

    init {
        exePath = "bash"
        addParameters(buildString {
            mfPath?.let { append("$it/") }
            append("$mfFilename $buildCommand $taskName")
        })
    }
}
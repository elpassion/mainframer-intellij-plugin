package com.elpassion.intelijidea.common

import com.intellij.execution.configurations.GeneralCommandLine

class MFCommandLine(val buildCommand: String, val taskName: String) : GeneralCommandLine() {

    init {
        exePath = "bash"
        addParameters("mainframer.sh ./gradlew build")
    }
}
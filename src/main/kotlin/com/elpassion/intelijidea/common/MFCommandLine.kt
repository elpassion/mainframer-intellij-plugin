package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.execution.configurations.GeneralCommandLine

class MFCommandLine(val mfPath: String? = null, val buildCommand: String, val taskName: String) : GeneralCommandLine() {

    val absoluteMfPath: String
        get() = buildString {
            mfPath?.let { append("$it/") }
            append(mfFilename)
        }

    val commandWithTask: String
        get() = "$buildCommand $taskName"

    init {
        exePath = "bash"
        addParameters(absoluteMfPath, commandWithTask)
    }

    fun getResultingString(): String = commandLineString
}
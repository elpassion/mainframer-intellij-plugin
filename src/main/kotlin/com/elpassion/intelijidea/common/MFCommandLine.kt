package com.elpassion.intelijidea.common

import com.intellij.execution.configurations.GeneralCommandLine

fun createMfCommandLine(mfPath: String? = null, buildCommand: String, taskName: String) =
        GeneralCommandLine("bash", mfPath, "$buildCommand $taskName")
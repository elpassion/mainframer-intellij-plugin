package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.execution.configurations.GeneralCommandLine

fun createMfCommandLine(mfPath: String? = null, buildCommand: String, taskName: String) =
        GeneralCommandLine("bash", getAbsoluteMfPath(mfPath), "$buildCommand $taskName")

private fun getAbsoluteMfPath(mfPath: String?) = if (mfPath != null) "$mfPath/$mfFilename" else mfFilename
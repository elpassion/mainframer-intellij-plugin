package com.elpassion.mainframerplugin.configuration

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.runners.DefaultProgramRunner

class MainframerProgramRunner : DefaultProgramRunner() {

    override fun getRunnerId(): String = "MFRunner"

    override fun canRun(executorId: String, profile: RunProfile) = profile is MainframerRunConfiguration
}
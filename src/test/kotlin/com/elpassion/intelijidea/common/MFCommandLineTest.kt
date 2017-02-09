package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.util.mfFilename
import org.junit.Assert.assertEquals
import org.junit.Test

class MFCommandLineTest {

    @Test
    fun shouldGenerateCommandLineToExecute() {
        val commandLine = createCommandLine()
        commandLine.verifyResultingString()
    }

    @Test
    fun shouldGenerateProperCommandLineToExecute() {
        val commandLine = createCommandLine(buildCommand = "gradle", taskName = "assembleDebug")
        commandLine.verifyResultingString()
    }

    @Test
    fun shouldGenerateProperCommandLineWithCustomPathToExecute() {
        val commandLine = createCommandLine(mfPath = "/customPath")
        commandLine.verifyResultingString()
    }

    @Test
    fun shouldGenerateProperCommandLineWithWhiteSpacePathToExecute() {
        val commandLine = createCommandLine(mfPath = "/White Spaced Path")
        commandLine.verifyResultingString()
    }

    @Test
    fun shouldGenerateProperCommandLineWithMultipleTasksToExecute() {
        val commandLine = createCommandLine(taskName = "clean build")
        commandLine.verifyResultingString()
    }

    private fun MFCommandLine.verifyResultingString() {
        val expectedParams = listOf(
                "${if (mfPath != null) "$mfPath/" else ""}$mfFilename",
                "$buildCommand $taskName"
        ).map { if (it.contains(" ")) "\"$it\"" else it }
        assertEquals("bash ${expectedParams.joinToString(separator = " ")}", commandLineString)
    }

    private fun createCommandLine(mfPath: String = "", buildCommand: String = "./gradlew", taskName: String = "build")
            = MFCommandLine(mfPath, buildCommand, taskName)
}
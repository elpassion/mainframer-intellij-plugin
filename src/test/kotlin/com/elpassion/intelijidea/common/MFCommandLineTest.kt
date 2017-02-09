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
        val mfLocation = if (mfPath != null) "$mfPath/" else ""
        val mfAbsolutePath = "$mfLocation$mfFilename"
        val expectedParams = listOf(mfAbsolutePath, "$buildCommand $taskName").joinParams()
        assertEquals("bash $expectedParams", commandLineString)
    }

    private val String.withOptionalQuotes: String get() = if (contains(" ")) "\"$this\"" else this

    private fun List<String>.joinParams() = map { it.withOptionalQuotes }.joinToString(separator = " ")

    private fun createCommandLine(mfPath: String = "", buildCommand: String = "./gradlew", taskName: String = "build")
            = MFCommandLine(mfPath, buildCommand, taskName)
}
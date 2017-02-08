package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.util.mfFilename
import org.junit.Assert.assertEquals
import org.junit.Test

class MFCommandLineTest {

    @Test
    fun shouldGenerateCommandLineToExecute() {
        val commandLine = MFCommandLine(
                buildCommand = "./gradlew",
                taskName = "build")
        commandLine.verify()
    }

    @Test
    fun shouldGenerateProperCommandLineToExecute() {
        val commandLine = MFCommandLine(
                buildCommand = "gradle",
                taskName = "assembleDebug")
        commandLine.verify()
    }

    @Test
    fun shouldGenerateProperCommandLineWithCustomPathToExecute() {
        val commandLine = MFCommandLine(
                mfPath = "/customPath",
                buildCommand = "gradle",
                taskName = "build")
        commandLine.verify()
    }

    private fun MFCommandLine.verify() {
        assertEquals("bash \"${if (mfPath != null) "$mfPath/" else ""}${mfFilename} " +
                "$buildCommand $taskName\"", commandLineString)
    }
}
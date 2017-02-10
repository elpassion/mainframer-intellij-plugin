package com.elpassion.intelijidea.common

import org.junit.Assert.assertEquals
import org.junit.Test

class MFCommandLineTest {

    @Test
    fun shouldGenerateCommandLineToExecute() {
        val commandLine = MFCommandLine(
                mfPath = null,
                buildCommand = "./gradlew",
                taskName = "build")
        assertEquals("bash mainframer.sh \"./gradlew build\"", commandLine.getResultingString())
    }

    @Test
    fun shouldGenerateProperCommandLineToExecute() {
        val commandLine = MFCommandLine(
                mfPath = null,
                buildCommand = "gradle",
                taskName = "assembleDebug")
        assertEquals("bash mainframer.sh \"gradle assembleDebug\"", commandLine.getResultingString())
    }

    @Test
    fun shouldGenerateProperCommandLineWithCustomPathToExecute() {
        val commandLine = MFCommandLine(
                mfPath = "/customPath",
                buildCommand = "./gradlew",
                taskName = "build")
        assertEquals("bash /customPath/mainframer.sh \"./gradlew build\"", commandLine.getResultingString())
    }

    @Test
    fun shouldGenerateProperCommandLineWithWhiteSpacePathToExecute() {
        val commandLine = MFCommandLine(
                mfPath = "/White Spaced Path",
                buildCommand = "./gradlew",
                taskName = "build")
        assertEquals("bash \"/White Spaced Path/mainframer.sh\" \"./gradlew build\"", commandLine.getResultingString())
    }

    @Test
    fun shouldGenerateProperCommandLineWithMultipleTasksToExecute() {
        val commandLine = MFCommandLine(
                mfPath = null,
                buildCommand = "./gradlew",
                taskName = "clean build")
        assertEquals("bash mainframer.sh \"./gradlew clean build\"", commandLine.getResultingString())
    }
}
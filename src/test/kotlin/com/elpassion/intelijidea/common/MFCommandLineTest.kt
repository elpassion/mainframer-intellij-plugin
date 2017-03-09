package com.elpassion.intelijidea.common

import com.elpassion.intelijidea.common.console.createMfCommandLine
import org.junit.Assert.assertEquals
import org.junit.Test

class MFCommandLineTest {

    @Test
    fun shouldGenerateCommandLineToExecute() {
        val commandLine = createMfCommandLine(
                mfPath = "mainframer.sh",
                buildCommand = "./gradlew",
                taskName = "build")
        assertEquals("bash mainframer.sh \"./gradlew build\"", commandLine.commandLineString)
    }

    @Test
    fun shouldGenerateProperCommandLineToExecute() {
        val commandLine = createMfCommandLine(
                mfPath = "mainframer.sh",
                buildCommand = "gradle",
                taskName = "assembleDebug")
        assertEquals("bash mainframer.sh \"gradle assembleDebug\"", commandLine.commandLineString)
    }

    @Test
    fun shouldGenerateProperCommandLineWithCustomPathToExecute() {
        val commandLine = createMfCommandLine(
                mfPath = "/customPath/mainframer.sh",
                buildCommand = "./gradlew",
                taskName = "build")
        assertEquals("bash /customPath/mainframer.sh \"./gradlew build\"", commandLine.commandLineString)
    }

    @Test
    fun shouldGenerateProperCommandLineWithWhiteSpacePathToExecute() {
        val commandLine = createMfCommandLine(
                mfPath = "/White Spaced Path/mainframer.sh",
                buildCommand = "./gradlew",
                taskName = "build")
        assertEquals("bash \"/White Spaced Path/mainframer.sh\" \"./gradlew build\"", commandLine.commandLineString)
    }

    @Test
    fun shouldGenerateProperCommandLineWithMultipleTasksToExecute() {
        val commandLine = createMfCommandLine(
                mfPath = "mainframer.sh",
                buildCommand = "./gradlew",
                taskName = "clean build")
        assertEquals("bash mainframer.sh \"./gradlew clean build\"", commandLine.commandLineString)
    }
}
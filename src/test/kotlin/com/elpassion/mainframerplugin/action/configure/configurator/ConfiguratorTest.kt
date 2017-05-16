package com.elpassion.mainframerplugin.action.configure.configurator

import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.mainframerplugin.common.ToolConfigurationImpl
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.elpassion.mainframerplugin.util.toolFilename
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import org.junit.Assert
import java.io.File

class ConfiguratorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val configurationFromUi = mock<(ConfiguratorIn) -> Maybe<ConfiguratorOut>>()

    override fun setUp() {
        super.setUp()
        stubConfigurationFromUi()
    }

    fun testShouldReturnChosenVersion() {
        stubConfigurationFromUi(version = "1.0.0")
        configurator(project, configurationFromUi)(emptyList()).test().assertValue { it.version == "1.0.0" }
    }

    fun testShouldReturnDefaultMainframerPathVersion() {
        stubConfigurationFromUi(version = "1.0.0")
        configurator(project, configurationFromUi)(emptyList()).test().assertValue { it.file == File(project.basePath, toolFilename) }
    }

    fun testConfigurationFromUiRunWithGivenVersionList() {
        configureMainframerInProject(versionList = listOf("1.0.0"))

        verify(configurationFromUi).invoke(argThat { versionList == listOf("1.0.0") })
    }

    fun testConfigurationFromUiRunReallyWithGivenVersionList() {
        configureMainframerInProject(versionList = listOf("1.1.0"))

        verify(configurationFromUi).invoke(argThat { versionList == listOf("1.1.0") })
    }

    fun testConfigurationFromUiRunWithBuildCommandFromProvider() {
        stubMainframerTaskDefaultSettingsProvider(TaskData(buildCommand = "./gradlew"))
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { buildCommand == "./gradlew" })
    }

    fun testConfigurationFromUiRunReallyWithBuildCommandFromProvider() {
        stubMainframerTaskDefaultSettingsProvider(TaskData(buildCommand = "./buckw"))
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { buildCommand == "./buckw" })
    }

    fun testConfigurationFromUiRunWithRemoteMachineNameFromToolProperties() {
        ToolConfigurationImpl(project.basePath).writeRemoteMachineName("test_value")
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { remoteName == "test_value" })
    }

    fun testConfigurationFromUiReallyRunWithRemoteMachineNameFromToolProperties() {
        ToolConfigurationImpl(project.basePath).writeRemoteMachineName("test_2_value")
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { remoteName == "test_2_value" })
    }

    fun testShouldCreateToolPropertiesFileWithRemoteMachineName() {
        stubConfigurationFromUi(remoteName = "remote")
        configureMainframerInProject()

        val configurationFile = File(File(project.basePath, ".mainframer"), "config")
        Assert.assertTrue(configurationFile.exists())
        Assert.assertTrue(configurationFile.readLines().any { it == "remote_machine=remote" })
    }

    fun testShouldSaveChosenBuildCommandToSettingsProvider() {
        stubConfigurationFromUi(buildCommand = "buildCmd")
        configureMainframerInProject()
        Assert.assertEquals("buildCmd", settingsProviderTask().buildCommand)
    }

    fun testShouldReallySaveChosenBuildCommandToSettingsProvider() {
        stubConfigurationFromUi(buildCommand = "otherCmd")
        configureMainframerInProject()
        Assert.assertEquals("otherCmd", settingsProviderTask().buildCommand)
    }

    fun testShouldSaveMainframerPathToSettingsProvider() {
        configureMainframerInProject()
        Assert.assertEquals(File(project.basePath, "mainframer.sh").absolutePath, settingsProviderTask().mainframerPath)
    }

    private fun configureMainframerInProject(versionList: List<String> = emptyList()) {
        configurator(project, configurationFromUi)(versionList).subscribe()
    }

    private fun stubMainframerTaskDefaultSettingsProvider(taskData: TaskData) {
        MainframerTaskDefaultSettingsProvider.getInstance(project).taskData = taskData
    }

    private fun settingsProviderTask() = MainframerTaskDefaultSettingsProvider.getInstance(project).taskData

    private fun stubConfigurationFromUi(version: String = "0.0.1",
                                        buildCommand: String = "./gradlew",
                                        remoteName: String = "not_local") {
        whenever(configurationFromUi.invoke(any())).thenJust(
                ConfiguratorOut(version = version,
                        buildCommand = buildCommand,
                        remoteName = remoteName))
    }

    override fun tearDown() {
        stubMainframerTaskDefaultSettingsProvider(TaskData())
        super.tearDown()
    }
}
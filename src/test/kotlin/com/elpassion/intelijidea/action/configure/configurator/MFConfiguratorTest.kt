package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.android.commons.rxjavatest.thenJust
import com.elpassion.intelijidea.common.MFToolConfiguration
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import org.junit.Assert
import java.io.File

class MFConfiguratorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val configurationFromUi = mock<(MFConfiguratorIn) -> Maybe<MFConfiguratorOut>>()

    override fun setUp() {
        super.setUp()
        stubConfigurationFromUi()
    }

    fun testShouldReturnChosenVersion() {
        stubConfigurationFromUi(version = "1.0.0")
        mfConfigurator(project, configurationFromUi)(emptyList()).test().assertValue { it.version == "1.0.0" }
    }

    fun testShouldReturnDefaultMainframerPathVersion() {
        stubConfigurationFromUi(version = "1.0.0")
        mfConfigurator(project, configurationFromUi)(emptyList()).test().assertValue { it.file == File(project.basePath, mfFilename) }
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
        stubMfTaskSettingsProvider(MFTaskData(buildCommand = "./gradlew"))
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { buildCommand == "./gradlew" })
    }

    fun testConfigurationFromUiRunReallyWithBuildCommandFromProvider() {
        stubMfTaskSettingsProvider(MFTaskData(buildCommand = "./buckw"))
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { buildCommand == "./buckw" })
    }

    fun testConfigurationFromUiRunWithTaskNameFromProvider() {
        stubMfTaskSettingsProvider(MFTaskData(taskName = null))
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { taskName == null })
    }

    fun testConfigurationFromUiRunReallyWithTaskNameFromProvider() {
        stubMfTaskSettingsProvider(MFTaskData(taskName = "build"))
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { taskName == "build" })
    }

    fun testConfigurationFromUiRunWithRemoteMachineNameFromMfToolProperties() {
        MFToolConfiguration(project.basePath).writeRemoteMachineName("test_value")
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { remoteName == "test_value" })
    }

    fun testConfigurationFromUiReallyRunWithRemoteMachineNameFromMfToolProperties() {
        MFToolConfiguration(project.basePath).writeRemoteMachineName("test_2_value")
        configureMainframerInProject()

        verify(configurationFromUi).invoke(argThat { remoteName == "test_2_value" })
    }

    fun testShouldCreateMfToolPropertiesFileWithRemoteMachineName() {
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

    fun testShouldSaveChosenTaskNameToSettingsProvider() {
        stubConfigurationFromUi(taskName = "taskName1")
        configureMainframerInProject()
        Assert.assertEquals("taskName1", settingsProviderTask().taskName)
    }

    fun testShouldReallySaveChosenTaskNameToSettingsProvider() {
        stubConfigurationFromUi(taskName = "taskName2")
        configureMainframerInProject()
        Assert.assertEquals("taskName2", settingsProviderTask().taskName)
    }

    fun testShouldSaveMainframerPathToSettingsProvider() {
        configureMainframerInProject()
        Assert.assertEquals(File(project.basePath, "mainframer.sh").absolutePath, settingsProviderTask().mainframerPath)
    }

    private fun configureMainframerInProject(versionList: List<String> = emptyList()) {
        mfConfigurator(project, configurationFromUi)(versionList).subscribe()
    }

    private fun stubMfTaskSettingsProvider(mfTaskData: MFTaskData) {
        MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData = mfTaskData
    }

    private fun settingsProviderTask() = MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData

    private fun stubConfigurationFromUi(version: String = "0.0.1",
                                        buildCommand: String = "./gradlew",
                                        taskName: String = "assemble",
                                        remoteName: String = "not_local") {
        whenever(configurationFromUi.invoke(any())).thenJust(
                MFConfiguratorOut(version = version,
                        taskName = taskName,
                        buildCommand = buildCommand,
                        remoteName = remoteName))
    }

    override fun tearDown() {
        super.tearDown()
        stubMfTaskSettingsProvider(MFTaskData())
    }
}
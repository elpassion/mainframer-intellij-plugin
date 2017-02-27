package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.MFTaskInjector
import com.elpassion.intelijidea.TemplateConfigurationsProvider
import com.elpassion.intelijidea.getConfigurationsAsSelectorItems
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertNotEquals
import java.util.*

class InjectingMainframerBeforeRunTaskTestCase : LightPlatformCodeInsightFixtureTestCase() {

    private val taskInjector by lazy { MFTaskInjector(project, MFBeforeRunTaskProvider(project)) }
    private val runManager by lazy { taskInjector.runManager }

    fun testShouldAddMainframerToConfigurationWhichRequiresCompilationBeforeLaunch() {
        val runConfiguration = addTestConfiguration(createTestConfigurationType(compileBeforeRun = true))
        injectMainframer()

        verifyBeforeRunTasks(runConfiguration)
                .hasSize(1)
                .allMatch { it is MFBeforeRunTask }
    }

    fun testShouldNotAddMainframerToConfigurationWhichDoesNotRequireCompilationBeforeLaunch() {
        val runConfiguration = addTestConfiguration(createTestConfigurationType(compileBeforeRun = false))
        injectMainframer()

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldRemoveMainframerFromConfiguration() {
        val runConfiguration = addTestConfiguration(createTestConfigurationType(compileBeforeRun = true))
        injectMainframer()
        restoreConfigurations()

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldAddMainframerToTemplateConfigurationWhichRequiresCompilationBeforeLaunch() {
        val runConfiguration = addTestTemplateConfiguration(compileBeforeRun = true)

        injectMainframer()

        verifyBeforeRunTasks(runConfiguration)
                .hasSize(1)
                .allMatch { it is MFBeforeRunTask }
    }

    fun testShouldNotAddMainframerToTemplateConfigurationWhichDoesNotRequireCompilationBeforeLaunch() {
        val runConfiguration = addTestTemplateConfiguration(compileBeforeRun = false)
        injectMainframer()

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldRemoveMainframerFromTemplateConfiguration() {
        val runConfiguration = addTestTemplateConfiguration(compileBeforeRun = true)
        injectMainframer()
        restoreConfigurations()

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldNotReplaceMainframerMakeDataWhenTaskExistedBeforeInjection() {
        val runConfiguration = addTestTemplateConfiguration(compileBeforeRun = true)
        injectMainframer()
        val oldTaskData = firstMFBeforeRunTaskData(runConfiguration)
        MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData = MFTaskData("path2")
        injectMainframer()
        val newTaskData = firstMFBeforeRunTaskData(runConfiguration)

        assertEquals(oldTaskData, newTaskData)
    }

    fun testShouldReplaceMainframerMakeDataOnNextInjectionWhenReplacingAllMfTasks() {
        val runConfiguration = addTestTemplateConfiguration(compileBeforeRun = true)
        injectMainframerReplacingAllMfTasks()
        val oldTaskData = firstMFBeforeRunTaskData(runConfiguration)
        MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData = MFTaskData("path2")
        injectMainframerReplacingAllMfTasks()
        val newTaskData = firstMFBeforeRunTaskData(runConfiguration)

        assertNotEquals(oldTaskData, newTaskData)
    }

    private fun firstMFBeforeRunTaskData(runConfiguration: RunConfiguration) = (runManager.getBeforeRunTasks(runConfiguration).first() as MFBeforeRunTask).data

    private fun createTestConfigurationType(compileBeforeRun: Boolean) = TestConfigurationType(randomString(), compileBeforeRun)

    private fun addTestConfiguration(testConfigurationType: TestConfigurationType): RunConfiguration {
        val testConfigurationFactory = testConfigurationType.configurationFactories.first()
        val runConfiguration = runManager.createRunConfiguration(randomString(), testConfigurationFactory)
        runManager.addConfiguration(runConfiguration, false)
        return runConfiguration.configuration
    }

    private fun addTestTemplateConfiguration(compileBeforeRun: Boolean): RunConfiguration {
        val testConfigurationType = createTestConfigurationType(compileBeforeRun)
        TemplateConfigurationsProvider.testValue = listOf(testConfigurationType)
        val configurationFactory = testConfigurationType.configurationFactories.first()
        return runManager.getConfigurationTemplate(configurationFactory).configuration
    }

    private class TestConfigurationFactory(testConfigurationType: TestConfigurationType, val uuid: String, val compileBeforeLaunch: Boolean) : ConfigurationFactory(testConfigurationType) {
        override fun createTemplateConfiguration(project: Project) = TestRunConfigurationBase(this, project, uuid, compileBeforeLaunch)
    }

    private class TestRunConfigurationBase(configurationFactory: ConfigurationFactory, project: Project, uuid: String, val compileBeforeLaunch: Boolean) : RunConfigurationBase(project, configurationFactory, uuid) {
        override fun checkConfiguration() = Unit

        override fun getConfigurationEditor() = TODO("mock")

        override fun getState(executor: Executor, environment: ExecutionEnvironment) = null

        override fun isCompileBeforeLaunchAddedByDefault() = compileBeforeLaunch
    }

    private class TestConfigurationType(val uuid: String, compileBeforeLaunch: Boolean) : ConfigurationType {
        private val elements = TestConfigurationFactory(this, uuid, compileBeforeLaunch)

        override fun getIcon() = AllIcons.Icons.Ide.NextStep

        override fun getConfigurationTypeDescription() = "mock"

        override fun getId() = uuid

        override fun getDisplayName() = "mock"

        override fun getConfigurationFactories() = arrayOf<ConfigurationFactory>(elements)
    }

    private fun injectMainframer() {
        taskInjector.injectMainframerBeforeTasks(runManager.getConfigurationsAsSelectorItems(true), false)
    }

    private fun injectMainframerReplacingAllMfTasks() {
        taskInjector.injectMainframerBeforeTasks(runManager.getConfigurationsAsSelectorItems(true), true)
    }

    private fun restoreConfigurations() {
        taskInjector.injectMainframerBeforeTasks(runManager.getConfigurationsAsSelectorItems(false), true)
    }

    private fun verifyBeforeRunTasks(configuration: RunConfiguration) = assertThat(runManager.getBeforeRunTasks(configuration))

    private fun randomString() = UUID.randomUUID().toString()
}
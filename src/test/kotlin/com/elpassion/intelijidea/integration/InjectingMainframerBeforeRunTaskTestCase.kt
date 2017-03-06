package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.TaskManipulator
import com.elpassion.intelijidea.TemplateConfigurationsProvider
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.execution.Executor
import com.intellij.execution.RunManagerEx
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
import javax.swing.Icon

class InjectingMainframerBeforeRunTaskTestCase : LightPlatformCodeInsightFixtureTestCase() {

    private val taskInjector by lazy { TaskManipulator(project) }
    private val runManager by lazy { RunManagerEx.getInstanceEx(project) }

    fun testShouldAddMainframerToConfiguration() {
        val runConfiguration = addTestConfiguration(createTestConfigurationType())
        injectMainframer(runConfiguration)

        verifyBeforeRunTasks(runConfiguration)
                .hasSize(1)
                .allMatch { it is MFBeforeRunTask }
    }

    fun testShouldRemoveMainframerFromConfiguration() {
        val runConfiguration = addTestConfiguration(createTestConfigurationType())
        injectMainframer(runConfiguration)
        restoreConfigurations(runConfiguration)

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldAddMainframerToTemplateConfiguration() {
        val runConfiguration = addTestTemplateConfiguration()

        injectMainframer(runConfiguration)

        verifyBeforeRunTasks(runConfiguration)
                .hasSize(1)
                .allMatch { it is MFBeforeRunTask }
    }

    fun testShouldRemoveMainframerFromTemplateConfiguration() {
        val runConfiguration = addTestTemplateConfiguration()
        injectMainframer(runConfiguration)
        restoreConfigurations(runConfiguration)

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldNotReplaceMainframerMakeDataWhenTaskExistedBeforeInjection() {
        val runConfiguration = addTestTemplateConfiguration()
        injectMainframer(runConfiguration)
        val oldTaskData = firstMFBeforeRunTaskData(runConfiguration)
        MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData = MFTaskData("path2")
        injectMainframer(runConfiguration)
        val newTaskData = firstMFBeforeRunTaskData(runConfiguration)

        assertEquals(oldTaskData, newTaskData)
    }

    fun testShouldReplaceMainframerMakeDataOnNextInjectionWhenReplacingAllMfTasks() {
        val runConfiguration = addTestTemplateConfiguration()
        injectMainframerReplacingAllMfTasks(runConfiguration)
        val oldTaskData = firstMFBeforeRunTaskData(runConfiguration)
        MFBeforeTaskDefaultSettingsProvider.INSTANCE.taskData = MFTaskData("path2")
        injectMainframerReplacingAllMfTasks(runConfiguration)
        val newTaskData = firstMFBeforeRunTaskData(runConfiguration)

        assertNotEquals(oldTaskData, newTaskData)
    }

    private fun firstMFBeforeRunTaskData(runConfiguration: RunConfiguration) = (runManager.getBeforeRunTasks(runConfiguration).first() as MFBeforeRunTask).data

    private fun createTestConfigurationType() = TestConfigurationType(randomString(), true)

    private fun addTestConfiguration(testConfigurationType: TestConfigurationType): RunConfiguration {
        val testConfigurationFactory = testConfigurationType.configurationFactories.first()
        val runConfiguration = runManager.createRunConfiguration(randomString(), testConfigurationFactory)
        runManager.addConfiguration(runConfiguration, false)
        return runConfiguration.configuration
    }

    private fun addTestTemplateConfiguration(): RunConfiguration {
        val testConfigurationType = createTestConfigurationType()
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

        override fun getIcon(): Icon = AllIcons.Icons.Ide.NextStep

        override fun getConfigurationTypeDescription() = "mock"

        override fun getId() = uuid

        override fun getDisplayName() = "mock"

        override fun getConfigurationFactories() = arrayOf<ConfigurationFactory>(elements)
    }

    private fun injectMainframerReplacingAllMfTasks(runConfiguration: RunConfiguration) {
        taskInjector.injectMFToConfigurationsWithReplacingMFTask(MFBeforeRunTaskProvider(project), listOf(runConfiguration))
    }

    private fun restoreConfigurations(runConfiguration: RunConfiguration) {
        taskInjector.restoreConfigurations(listOf(runConfiguration))
    }

    private fun injectMainframer(runConfiguration: RunConfiguration) {
        taskInjector.injectMFToConfigurationsWithReplacingMFTask(MFBeforeRunTaskProvider(project), listOf(runConfiguration))
    }

    private fun verifyBeforeRunTasks(configuration: RunConfiguration) = assertThat(runManager.getBeforeRunTasks(configuration))

    private fun randomString() = UUID.randomUUID().toString()
}
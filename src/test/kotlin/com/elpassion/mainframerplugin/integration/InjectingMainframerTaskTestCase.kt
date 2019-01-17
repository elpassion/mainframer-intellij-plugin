package com.elpassion.mainframerplugin.integration

import com.elpassion.mainframerplugin.action.select.TaskManipulator
import com.elpassion.mainframerplugin.action.select.TemplateConfigurationsProvider
import com.elpassion.mainframerplugin.task.MainframerTask
import com.elpassion.mainframerplugin.task.MainframerTaskProvider
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.execution.Executor
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertNotEquals
import java.util.*
import javax.swing.Icon

class InjectingMainframerTaskTestCase : LightPlatformCodeInsightFixtureTestCase() {

    private val taskInjector by lazy { TaskManipulator(project) }
    private val runManager by lazy { RunManagerEx.getInstanceEx(project) }

    fun testShouldAddMainframerToConfiguration() {
        val runConfiguration = addTestConfiguration(createTestConfigurationType())
        injectMainframer(runConfiguration)

        verifyBeforeRunTasks(runConfiguration)
                .hasSize(1)
                .allMatch { it is MainframerTask }
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
                .allMatch { it is MainframerTask }
    }

    fun testShouldRemoveMainframerFromTemplateConfiguration() {
        val runConfiguration = addTestTemplateConfiguration()
        injectMainframer(runConfiguration)
        restoreConfigurations(runConfiguration)

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldReplaceMainframerMakeDataWhenTaskExistedBeforeInjection() {
        val runConfiguration = addTestTemplateConfiguration()
        injectMainframer(runConfiguration)
        val oldTaskData = firstTaskData(runConfiguration)
        MainframerTaskDefaultSettingsProvider.getInstance(project).taskData = TaskData(mainframerPath = "path2")
        injectMainframer(runConfiguration)
        val newTaskData = firstTaskData(runConfiguration)

        assertNotEquals(oldTaskData, newTaskData)
    }

    private fun firstTaskData(runConfiguration: RunConfiguration) = (runManager.getBeforeRunTasks(runConfiguration).first() as MainframerTask).data

    private fun createTestConfigurationType() = TestConfigurationType(randomString())

    private fun addTestConfiguration(testConfigurationType: TestConfigurationType): RunConfiguration {
        val testConfigurationFactory = testConfigurationType.configurationFactories.first()
        val runConfiguration = runManager.createConfiguration(randomString(), testConfigurationFactory)
        runManager.addConfiguration(runConfiguration, false)
        return runConfiguration.configuration
    }

    private fun addTestTemplateConfiguration(): RunConfiguration {
        val testConfigurationType = createTestConfigurationType()
        TemplateConfigurationsProvider.testValue = listOf(testConfigurationType)
        val configurationFactory = testConfigurationType.configurationFactories.first()
        return runManager.getConfigurationTemplate(configurationFactory).configuration
    }

    private class TestConfigurationFactory(testConfigurationType: TestConfigurationType, val uuid: String) : ConfigurationFactory(testConfigurationType) {
        override fun createTemplateConfiguration(project: Project): RunConfiguration = TestRunConfigurationBase(this, project, uuid)
    }

    private class TestRunConfigurationBase(configurationFactory: ConfigurationFactory, project: Project, uuid: String) : RunConfigurationBase<RunConfigurationOptions>(project, configurationFactory, uuid) {
        override fun checkConfiguration() = Unit

        override fun getConfigurationEditor() = TODO("mock")

        override fun getState(executor: Executor, environment: ExecutionEnvironment) = null
    }

    private class TestConfigurationType(val uuid: String) : ConfigurationType {
        private val elements = TestConfigurationFactory(this, uuid)

        override fun getIcon(): Icon = AllIcons.Icons.Ide.NextStep

        override fun getConfigurationTypeDescription() = "mock"

        override fun getId() = uuid

        override fun getDisplayName() = "mock"

        override fun getConfigurationFactories() = arrayOf<ConfigurationFactory>(elements)
    }

    private fun restoreConfigurations(runConfiguration: RunConfiguration) {
        taskInjector.restoreConfigurations(listOf(runConfiguration))
    }

    private fun injectMainframer(runConfiguration: RunConfiguration) {
        taskInjector.injectToolToConfigurations(MainframerTaskProvider(project), listOf(runConfiguration))
    }

    private fun verifyBeforeRunTasks(configuration: RunConfiguration) = assertThat(runManager.getBeforeRunTasks(configuration))

    private fun randomString() = UUID.randomUUID().toString()
}
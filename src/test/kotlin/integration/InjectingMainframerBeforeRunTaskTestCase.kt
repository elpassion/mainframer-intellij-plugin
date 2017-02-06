package integration

import com.elpassion.intelijidea.TemplateConfigurationsProvider
import com.elpassion.intelijidea.injectMainframerBeforeTasks
import com.elpassion.intelijidea.restoreDefaultBeforeRunTasks
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.intellij.execution.Executor
import com.intellij.execution.RunManagerEx
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat
import java.util.*

class InjectingMainframerBeforeRunTaskTestCase : LightPlatformCodeInsightFixtureTestCase() {
    private val runManager by lazy { RunManagerEx.getInstanceEx(project) }

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

    private fun createTestConfigurationType(compileBeforeRun: Boolean) = TestConfigurationType(randomString(), compileBeforeRun)

    private fun addTestConfiguration(testConfigurationType: TestConfigurationType): RunnerAndConfigurationSettings {
        val testConfigurationFactory = testConfigurationType.configurationFactories.first()
        val runConfiguration = runManager.createRunConfiguration(randomString(), testConfigurationFactory)
        runManager.addConfiguration(runConfiguration, false)
        return runConfiguration
    }

    private fun addTestTemplateConfiguration(compileBeforeRun: Boolean): RunnerAndConfigurationSettings {
        val testConfigurationType = createTestConfigurationType(compileBeforeRun)
        TemplateConfigurationsProvider.testValue = listOf(testConfigurationType)
        return runManager.getConfigurationTemplate(testConfigurationType.configurationFactories.first())
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
        injectMainframerBeforeTasks(runManager, MFBeforeRunTaskProvider(project))
    }

    private fun restoreConfigurations() {
        restoreDefaultBeforeRunTasks(runManager, project)
    }

    private fun verifyBeforeRunTasks(runConfiguration: RunnerAndConfigurationSettings) = assertThat(runManager.getBeforeRunTasks(runConfiguration.configuration))

    private fun randomString() = UUID.randomUUID().toString()
}
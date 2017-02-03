package integration

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
        val runConfiguration = addTestConfiguration(compileBeforeLaunch = true)
        injectMainframer()

        verifyBeforeRunTasks(runConfiguration)
                .hasSize(1)
                .allMatch { it is MFBeforeRunTask }
    }

    fun testShouldNotAddMainframerToConfigurationWhichDoesNotRequireCompilationBeforeLaunch() {
        val runConfiguration = addTestConfiguration(compileBeforeLaunch = false)
        injectMainframer()

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    fun testShouldRemoveMainframerFromConfiguration() {
        val runConfiguration = addTestConfiguration(compileBeforeLaunch = true)
        injectMainframer()
        restoreConfigurations()

        verifyBeforeRunTasks(runConfiguration)
                .isEmpty()
    }

    private fun addTestConfiguration(compileBeforeLaunch: Boolean): RunnerAndConfigurationSettings {
        val runConfiguration = runManager.createRunConfiguration("name", createConfigurationFactory(compileBeforeLaunch))
        runManager.addConfiguration(runConfiguration, false)
        return runConfiguration
    }

    private fun createConfigurationFactory(compileBeforeLaunch: Boolean): ConfigurationFactory {
        return object : ConfigurationFactory(createConfigurationType()) {
            override fun createTemplateConfiguration(project: Project) = createRunConfigurationBase(compileBeforeLaunch)
        }
    }

    private fun ConfigurationFactory.createRunConfigurationBase(compileBeforeLaunch: Boolean): RunConfigurationBase {
        return object : RunConfigurationBase(project, this, "name") {
            override fun checkConfiguration() = Unit

            override fun getConfigurationEditor() = TODO("mock")

            override fun getState(executor: Executor, environment: ExecutionEnvironment) = null

            override fun isCompileBeforeLaunchAddedByDefault() = compileBeforeLaunch
        }
    }

    private fun createConfigurationType(): ConfigurationType {
        return object : ConfigurationType {
            override fun getIcon() = AllIcons.Icons.Ide.NextStep

            override fun getConfigurationTypeDescription() = "mock"

            override fun getId() = UUID.randomUUID().toString()

            override fun getDisplayName() = "mock"

            override fun getConfigurationFactories() = emptyArray<ConfigurationFactory>()
        }
    }

    private fun injectMainframer() {
        injectMainframerBeforeTasks(runManager, MFBeforeRunTaskProvider(project))
    }

    private fun restoreConfigurations() {
        restoreDefaultBeforeRunTasks(runManager, project)
    }

    private fun verifyBeforeRunTasks(runConfiguration: RunnerAndConfigurationSettings) = assertThat(runManager.getBeforeRunTasks(runConfiguration.configuration))
}

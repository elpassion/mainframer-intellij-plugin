package com.elpassion.mainframerplugin.integration

import com.elpassion.mainframerplugin.configuration.MainframerConfigurationFactory
import com.elpassion.mainframerplugin.configuration.MainframerRunConfiguration
import com.elpassion.mainframerplugin.configuration.MainframerConfigurationType
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import java.io.File

class MainframerRunConfigurationExecutionTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldNotThrowExecutionExceptionWhenConfigurationDataIsNull() {
        buildProjectAndExecute(configurationData = null)
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData().withToolFile()
        buildProjectAndExecute(configurationData = data)
    }

    private fun createConfigurationData(): TaskData {
        return TaskData(mainframerPath = File(project.basePath, "mainframer.sh").absolutePath)
    }

    private fun TaskData.withToolFile() = apply {
        FileUtil.createTempFile(File(project.basePath), "mainframer.sh", null).setExecutable(true)
    }

    private fun buildProjectAndExecute(configurationData: TaskData? = null) {
        val config = createRunConfiguration(configurationData)
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }

    private fun createRunConfiguration(configurationData: TaskData?) =
            MainframerRunConfiguration(project, MainframerConfigurationFactory(MainframerConfigurationType()), "").apply {
                data = configurationData
            }
}

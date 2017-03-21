package com.elpassion.mainframerplugin.integration

import com.elpassion.mainframerplugin.configuration.MFConfigurationFactory
import com.elpassion.mainframerplugin.configuration.MFRunConfiguration
import com.elpassion.mainframerplugin.configuration.MFRunConfigurationType
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import java.io.File

class MFRunConfigurationExecutionTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldNotThrowExecutionExceptionWhenConfigurationDataIsNull() {
        buildProjectAndExecute(configurationData = null)
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData().withToolFile()
        buildProjectAndExecute(configurationData = data)
    }

    private fun createConfigurationData(): MFTaskData {
        return MFTaskData(mainframerPath = File(project.basePath, "mainframer.sh").absolutePath)
    }

    private fun MFTaskData.withToolFile() = apply {
        FileUtil.createTempFile(File(project.basePath), "mainframer.sh", null).setExecutable(true)
    }

    private fun buildProjectAndExecute(configurationData: MFTaskData? = null) {
        val config = createRunConfiguration(configurationData)
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }

    private fun createRunConfiguration(configurationData: MFTaskData?) =
            MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "").apply {
                data = configurationData
            }
}

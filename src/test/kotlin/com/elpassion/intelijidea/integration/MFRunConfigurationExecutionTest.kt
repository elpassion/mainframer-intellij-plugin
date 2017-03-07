package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.common.assertThrows
import com.elpassion.intelijidea.configuration.MFConfigurationFactory
import com.elpassion.intelijidea.configuration.MFRunConfiguration
import com.elpassion.intelijidea.configuration.MFRunConfigurationData
import com.elpassion.intelijidea.configuration.MFRunConfigurationType
import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import java.io.File

class MFRunConfigurationExecutionTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldThrowExecutionExceptionWhenConfigurationDataIsNull() {
        assertThrows<ExecutionException> {
            buildProjectAndExecute(configurationData = null)
        }
    }

    fun testShouldThrowExecutionExceptionWhenToolNotFound() {
        assertThrows<ExecutionException> {
            val data = createConfigurationData()
            buildProjectAndExecute(configurationData = data)
        }
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData().withToolFile()
        buildProjectAndExecute(configurationData = data)
    }

    private fun createConfigurationData(): MFRunConfigurationData {
        return MFRunConfigurationData(mainframerPath = File(project.basePath, "mainframer.sh").absolutePath)
    }

    private fun MFRunConfigurationData.withToolFile() = apply {
        FileUtil.createTempFile(File(project.basePath), "mainframer.sh", null).setExecutable(true)
    }

    private fun buildProjectAndExecute(configurationData: MFRunConfigurationData? = null) {
        val config = createRunConfiguration(configurationData)
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }

    private fun createRunConfiguration(configurationData: MFRunConfigurationData?) =
            MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {}).apply {
                data = configurationData
            }
}

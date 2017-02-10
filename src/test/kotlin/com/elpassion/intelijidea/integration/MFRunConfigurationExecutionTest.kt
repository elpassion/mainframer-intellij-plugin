package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.configuration.MFConfigurationFactory
import com.elpassion.intelijidea.configuration.MFRunConfigurationType
import com.elpassion.intelijidea.configuration.MFRunnerConfiguration
import com.elpassion.intelijidea.configuration.MFRunnerConfigurationData
import com.elpassion.intelijidea.configuration.common.assertThrows
import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class MFRunConfigurationExecutionTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldThrowExecutionExceptionWhenConfigurationDataIsNull() {
        val exception = assertThrows(ExecutionException::class.java) {
            val config = createRunConfiguration(configurationData = null)
            buildAndExecuteProject(config)
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
    }

    fun testShouldThrowExecutionExceptionWhenToolNotFound() {
        val exception = assertThrows(ExecutionException::class.java) {
            val data = createConfigurationData(withToolFile = false)
            val config = createRunConfiguration(configurationData = data)
            buildAndExecuteProject(config)
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData(withToolFile = true)
        val config = createRunConfiguration(configurationData = data)
        buildAndExecuteProject(config)
    }

    private fun createRunConfiguration(configurationData: MFRunnerConfigurationData?) =
            MFRunnerConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {}).apply {
                data = configurationData
            }

    private fun createConfigurationData(withToolFile: Boolean): MFRunnerConfigurationData {
        val mfDir = FileUtil.createTempDirectory("", "mf")
        if (withToolFile) {
            FileUtil.createTempFile(mfDir, "mainframer", ".sh")
        }
        val data = MFRunnerConfigurationData(mainframerPath = mfDir.absolutePath)
        return data
    }

    private fun buildAndExecuteProject(config: MFRunnerConfiguration) {
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }
}
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
        assertThrows(ExecutionException::class.java) {
            val config = createRunConfiguration(configurationData = null)
            buildProjectAndExecute(config)
        }
    }

    fun testShouldThrowExecutionExceptionWhenToolNotFound() {
        assertThrows(ExecutionException::class.java) {
            val data = createConfigurationData(withToolFile = false)
            val config = createRunConfiguration(configurationData = data)
            buildProjectAndExecute(config)
        }
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData(withToolFile = true)
        val config = createRunConfiguration(configurationData = data)
        buildProjectAndExecute(config)
    }

    private fun createRunConfiguration(configurationData: MFRunnerConfigurationData?) =
            MFRunnerConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {}).apply {
                data = configurationData
            }

    private fun createConfigurationData(withToolFile: Boolean): MFRunnerConfigurationData {
        with(FileUtil.createTempDirectory("", "mf")) {
            if (withToolFile) {
                FileUtil.createTempFile(this, "mainframer", ".sh")
            }
            return MFRunnerConfigurationData(mainframerPath = absolutePath)
        }
    }

    private fun buildProjectAndExecute(config: MFRunnerConfiguration) {
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }
}
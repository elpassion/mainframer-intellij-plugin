package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.configuration.MFConfigurationFactory
import com.elpassion.intelijidea.configuration.MFRunConfigurationType
import com.elpassion.intelijidea.configuration.MFRunConfiguration
import com.elpassion.intelijidea.configuration.MFRunConfigurationData
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

    private fun createRunConfiguration(configurationData: MFRunConfigurationData?) =
            MFRunConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {}).apply {
                data = configurationData
            }

    private fun createConfigurationData(withToolFile: Boolean): MFRunConfigurationData {
        with(FileUtil.createTempDirectory("", "mf")) {
            if (withToolFile) {
                FileUtil.createTempFile(this, "mainframer", ".sh")
            }
            return MFRunConfigurationData(mainframerPath = absolutePath)
        }
    }

    private fun buildProjectAndExecute(config: MFRunConfiguration) {
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }
}
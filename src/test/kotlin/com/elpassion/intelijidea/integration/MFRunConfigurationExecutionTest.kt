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

    fun testShouldThrowExecutionExceptionWhenToolNotFound() {
        val exception = assertThrows(ExecutionException::class.java) {
            val config = createRunConfiguration()
            val executor = DefaultRunExecutor.getRunExecutorInstance()
            ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData()
        val config = createRunConfiguration(data)
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }

    private fun createRunConfiguration(configurationData: MFRunnerConfigurationData? = null) =
            MFRunnerConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {}).apply {
                data = configurationData
            }

    private fun createConfigurationData(): MFRunnerConfigurationData {
        val mfDir = FileUtil.createTempDirectory("", "mf")
        FileUtil.createTempFile(mfDir, "mainframer", ".sh")
        val data = MFRunnerConfigurationData(mainframerPath = mfDir.absolutePath)
        return data
    }
}
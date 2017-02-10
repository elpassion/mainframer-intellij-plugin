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
            val config = MFRunnerConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {})
            val executor = DefaultRunExecutor.getRunExecutorInstance()
            ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val mfDir = FileUtil.createTempDirectory("", "mf")
        FileUtil.createTempFile(mfDir, "mainframer", ".sh")
        val configurationData = MFRunnerConfigurationData(mainframerPath = mfDir.absolutePath)
        val config = MFRunnerConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {}).apply {
            data = configurationData
        }
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
    }
}
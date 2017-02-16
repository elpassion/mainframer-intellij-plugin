package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.configuration.MFConfigurationFactory
import com.elpassion.intelijidea.configuration.MFRunConfiguration
import com.elpassion.intelijidea.configuration.MFRunConfigurationData
import com.elpassion.intelijidea.configuration.MFRunConfigurationType
import com.elpassion.intelijidea.configuration.common.assertThrows
import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import java.io.File

class MFRunConfigurationExecutionTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldThrowExecutionExceptionWhenConfigurationDataIsNull() {
        assertThrows(ExecutionException::class.java) {
            buildProjectAndExecute(configurationData = null)
        }
    }

    fun testShouldThrowExecutionExceptionWhenToolNotFound() {
        assertThrows(ExecutionException::class.java) {
            val data = createConfigurationData()
            buildProjectAndExecute(configurationData = data)
        }
    }

    fun testShouldStartExecutionWithoutExceptionOnToolFoundInDefinedPath() {
        val data = createConfigurationData().withToolFile()
        buildProjectAndExecute(configurationData = data)
    }

    private fun createConfigurationData(): MFRunConfigurationData {
        with(FileUtil.createTempDirectory("", "mf")) {
            return MFRunConfigurationData(mainframerPath = absolutePath)
        }
    }

    private fun MFRunConfigurationData.withToolFile() = apply {
        FileUtil.createTempFile(File(mainframerPath), "mainframer", ".sh")
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

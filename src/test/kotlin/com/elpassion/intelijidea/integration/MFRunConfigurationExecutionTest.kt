package com.elpassion.intelijidea.integration

import com.elpassion.intelijidea.configuration.MFConfigurationFactory
import com.elpassion.intelijidea.configuration.MFRunConfigurationType
import com.elpassion.intelijidea.configuration.MFRunnerConfiguration
import com.elpassion.intelijidea.configuration.common.assertThrows
import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class MFRunConfigurationExecutionTest : LightPlatformCodeInsightFixtureTestCase() {

    fun testShouldThrowExecutionExceptionWhenToolNotFoundOnExecution() {
        val exception = assertThrows(ExecutionException::class.java) {
            val config = MFRunnerConfiguration(project, MFConfigurationFactory(MFRunConfigurationType()), "", {})
            val executor = DefaultRunExecutor.getRunExecutorInstance()
            ExecutionEnvironmentBuilder.create(project, executor, config).buildAndExecute()
        }
        assertEquals("Mainframer tool cannot be found", exception.message)
    }
}
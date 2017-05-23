package com.elpassion.mainframerplugin.task

import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertFalse
import org.junit.Test

class MainframerTaskProviderTest {

    private val project = mock<Project>()
    private val taskProvider = MainframerTaskProvider(project)

    @Test
    fun shouldBlockTaskExecutionWhenTaskIsInvalid() {
        assertFalse(taskProvider.executeTask(mock(), mock(), mock(), createInvalidTask()))
    }

    private fun createInvalidTask() = MainframerTask(TaskData())
}
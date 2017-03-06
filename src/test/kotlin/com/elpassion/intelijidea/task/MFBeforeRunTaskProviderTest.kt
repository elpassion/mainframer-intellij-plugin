package com.elpassion.intelijidea.task

import com.intellij.openapi.project.Project
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertFalse
import org.junit.jupiter.api.Test

class MFBeforeRunTaskProviderTest {

    private val project = mock<Project>()
    private val taskProvider = MFBeforeRunTaskProvider(project)

    @Test
    fun shouldBlockTaskExecutionWhenTaskIsInvalid() {
        assertFalse(taskProvider.executeTask(mock(), mock(), mock(), createInvalidTask()))
    }

    private fun createInvalidTask() = MFBeforeRunTask(MFTaskData())
}
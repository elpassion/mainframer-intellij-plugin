package com.elpassion.intelijidea.common

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import javax.swing.JTextField

class TaskFieldValidatorTest {

    val taskNameField = mock<JTextField>()

    @Test
    fun shouldReturnProperValidationInfoIfBuildCommandIsEmpty() {
        whenever(taskNameField.text).thenReturn("")
        val result = TaskFieldValidator(taskNameField).validate()
        assertEquals("Task cannot be empty", result?.message)
    }

    @Test
    fun shouldReturnNullInfoIfBuildCommandIsValid() {
        whenever(taskNameField.text).thenReturn("aTask")
        val result = TaskFieldValidator(taskNameField).validate()
        assertNull(result)
    }
}
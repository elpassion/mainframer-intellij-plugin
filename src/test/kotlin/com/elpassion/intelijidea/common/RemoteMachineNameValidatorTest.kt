package com.elpassion.intelijidea.common

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import javax.swing.JTextField

class RemoteMachineNameValidatorTest {

    val remoteMachineField = mock<JTextField>()

    @Test
    fun shouldReturnProperValidationInfoIfBuildCommandIsEmpty() {
        whenever(remoteMachineField.text).thenReturn("")
        val result = RemoteMachineFieldValidator(remoteMachineField).validate()
        assertEquals("Remote machine name cannot be empty!", result?.message)
    }

    @Test
    fun shouldReturnProperValidationInfoIfBuildCommandIsBlank() {
        whenever(remoteMachineField.text).thenReturn("  ")
        val result = RemoteMachineFieldValidator(remoteMachineField).validate()
        assertEquals("Remote machine name cannot be empty!", result?.message)
    }

    @Test
    fun shouldReturnNullInfoIfBuildCommandIsValid() {
        whenever(remoteMachineField.text).thenReturn("remote")
        val result = RemoteMachineFieldValidator(remoteMachineField).validate()
        assertNull(result)
    }
}
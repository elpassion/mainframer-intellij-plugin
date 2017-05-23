package com.elpassion.mainframerplugin.common

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import javax.swing.JTextField

class BuildCommandValidatorTest {

    val buildCommandField = mock<JTextField>()

    @Test
    fun shouldReturnProperValidationInfoIfBuildCommandIsEmpty() {
        whenever(buildCommandField.text).thenReturn("")
        val result = BuildCommandValidator(buildCommandField).validate()
        assertEquals("Build command cannot be empty", result?.message)
    }

    @Test
    fun shouldReturnProperValidationInfoIfBuildCommandIsBlank() {
        whenever(buildCommandField.text).thenReturn("  ")
        val result = BuildCommandValidator(buildCommandField).validate()
        assertEquals("Build command cannot be empty", result?.message)
    }

    @Test
    fun shouldReturnNullInfoIfBuildCommandIsValid() {
        whenever(buildCommandField.text).thenReturn("build")
        val result = BuildCommandValidator(buildCommandField).validate()
        assertNull(result)
    }
}
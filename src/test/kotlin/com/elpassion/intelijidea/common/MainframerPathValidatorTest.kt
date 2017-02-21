package com.elpassion.intelijidea.common

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class MainframerPathValidatorTest {

    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    val mainframerPathField = mock<TextFieldWithBrowseButton>()

    @Test
    fun shouldReturnProperValidationInfoIfPathIsEmpty() {
        whenever(mainframerPathField.text).thenReturn("")
        val result = MainframerPathValidator(mainframerPathField).validate()
        assertEquals("Path cannot be empty", result?.message)
    }

    @Test
    fun shouldReturnProperValidationInfoIfFileDoesNotExist() {
        whenever(mainframerPathField.text).thenReturn("asd/asd/asd")
        val result = MainframerPathValidator(mainframerPathField).validate()
        assertEquals("Cannot find mainframer script in path", result?.message)
    }

    @Test
    fun shouldReturnNullInfoIfPathsValid() {
        whenever(mainframerPathField.text).thenReturn("build")
        val result = MainframerPathValidator(mainframerPathField).validate()
        assertNull(result)
    }

    @Test
    fun shouldReturnProperValidationInfoIfNotExecutable() {
        val mfScript = createMFScript()
        mfScript.setExecutable(false)
        whenever(mainframerPathField.text).thenReturn(mfScript.path)
        val result = MainframerPathValidator(mainframerPathField).validate()
        assertEquals("Mainframer script in not executable", result?.message)
    }

    @Test
    fun shouldReturnNullInfoIfExecutable() {
        val mfScript = createMFScript()
        mfScript.setExecutable(true)
        whenever(mainframerPathField.text).thenReturn(mfScript.path)
        val result = MainframerPathValidator(mainframerPathField).validate()
        assertNull(result)
    }

    private fun createMFScript() = temporaryFolder.newFile()
}
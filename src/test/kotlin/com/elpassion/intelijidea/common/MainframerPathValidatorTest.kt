package com.elpassion.intelijidea.common

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Test

class MainframerPathValidatorTest {

    val mainframerPathField = mock<TextFieldWithBrowseButton>()

    @Test
    fun shouldReturnProperValidationInfoIfPathIsEmpty() {
        whenever(mainframerPathField.text).thenReturn("")
        val result = MainframerPathValidator(mainframerPathField).validate()
        Assert.assertEquals("Path cannot be empty", result?.message)
    }

    @Test
    fun shouldReturnProperValidationInfoIfFileDoesNotExist() {
        whenever(mainframerPathField.text).thenReturn("asd/asd/asd")
        val result = MainframerPathValidator(mainframerPathField).validate()
        Assert.assertEquals("Cannot find mainframer script in path", result?.message)
    }

    @Test
    fun shouldReturnNullInfoIfPathsValid() {
        whenever(mainframerPathField.text).thenReturn("build")
        val result = MainframerPathValidator(mainframerPathField).validate()
        Assert.assertNull(result)
    }
}
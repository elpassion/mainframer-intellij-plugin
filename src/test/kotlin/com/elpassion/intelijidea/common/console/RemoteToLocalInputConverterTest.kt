package com.elpassion.intelijidea.common.console

import org.assertj.core.api.Assertions
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteToLocalInputConverterTest {

    private val PROJECT_NAME = "testProject"
    private val localBasePath = "base/path/$PROJECT_NAME"
    private val converter = RemoteToLocalInputConverter(PROJECT_NAME)

    @Test
    fun `Should catch file path from remote machine`() {
        assertTrue(converter.FILE_PATH_REGEX.matches("/mainframer/$PROJECT_NAME"))
    }

    @Test
    fun `Should not catch file path if it is not from remote machine`() {
        assertFalse(converter.FILE_PATH_REGEX.matches("/$PROJECT_NAME"))
    }

    @Test
    fun `Should catch file path if it starts with longer path`() {
        assertTrue(converter.FILE_PATH_REGEX.matches("/longer/path/mainframer/$PROJECT_NAME"))
    }

    @Test
    fun `Should catch file path if it ends with kotlin class name`() {
        assertTrue(converter.FILE_PATH_REGEX.matches("/longer/path/mainframer/$PROJECT_NAME/Example.kt"))
    }

    @Test
    fun `Should catch file path if it ends with java class name`() {
        assertTrue(converter.FILE_PATH_REGEX.matches("/longer/path/mainframer/$PROJECT_NAME/Example.java"))
    }

    @Test
    fun `Should not catch file path if it ends with undefined class name`() {
        assertFalse(converter.FILE_PATH_REGEX.matches("/longer/path/mainframer/$PROJECT_NAME/Example."))
    }

    @Test
    fun `Should replace remote base path with given local path`() {
        val replacedPath = "/longer/path/mainframer/$PROJECT_NAME/Example.kt".replace(converter.FILE_PATH_REGEX, "$localBasePath$1")
        Assertions.assertThat(replacedPath).isEqualTo("$localBasePath/Example.kt")
    }
}

class RemoteToLocalInputConverter(projectName: String) {
    private val PATH_SEGMENT = "/\\w+"
    private val FILE_EXTENSION = "\\.\\w+"
    private val END_PATH = "($PATH_SEGMENT$FILE_EXTENSION)*"
    private val REMOTE_START_PATH = "(?:$PATH_SEGMENT)*"
    private val REMOTE_PATH = "$REMOTE_START_PATH/mainframer/$projectName"
    val FILE_PATH_REGEX = "(?:$REMOTE_PATH$END_PATH)".toRegex()
}

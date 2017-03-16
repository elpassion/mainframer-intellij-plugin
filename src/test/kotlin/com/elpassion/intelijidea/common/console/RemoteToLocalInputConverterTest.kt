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

    @Test
    fun `Should catch colon and space signs in first fragment of line`() {
        assertTrue(converter.FIRST_FRAGMENT_REGEX.matches(": "))
    }

    @Test
    fun `Should catch whole first fragment of line`() {
        assertTrue(converter.FIRST_FRAGMENT_REGEX.matches("Very complicated exception: "))
    }

    @Test
    fun `Should check only one line if it matches first fragment regex`() {
        assertFalse(converter.FIRST_FRAGMENT_REGEX.matches("Very complicated exception\n: "))
    }

    @Test
    fun `Should replace remote base path with given local path and have first fragment`() {
        val replacedPath = "Complicated exception: /longer/path/mainframer/$PROJECT_NAME/Example.kt".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$1$localBasePath$2")
        Assertions.assertThat(replacedPath).isEqualTo("Complicated exception: $localBasePath/Example.kt")
    }

    @Test
    fun `Should catch line number`() {
        assertTrue(converter.LINE_NUMBER_REGEX.matches(":100"))
    }

    @Test
    fun `Should not catch wrong line number`() {
        assertFalse(converter.LINE_NUMBER_REGEX.matches(":wrongLineNumber"))
    }

    @Test
    fun `Should catch line number when there is also given column number`() {
        assertTrue(converter.LINE_NUMBER_REGEX.matches(": (9, 10)"))
    }

    @Test
    fun `Should format correctly simple path line number value`() {
        val replacedPathSimple = ":321".replace(converter.LINE_NUMBER_REGEX, ":$1")
        Assertions.assertThat(replacedPathSimple).isEqualTo(":321")
    }

    @Test
    fun `Should format correctly complex path line number value`() {
        val replacedPathComplex = ": (90, 100)".replace(converter.LINE_NUMBER_REGEX, ":$1")
        Assertions.assertThat(replacedPathComplex).isEqualTo(":90")
    }

}

class RemoteToLocalInputConverter(projectName: String) {
    private val PATH_SEGMENT = "/\\w+"
    private val FILE_EXTENSION = "\\.\\w+"
    private val END_PATH = "($PATH_SEGMENT$FILE_EXTENSION)*"
    private val REMOTE_START_PATH = "(?:$PATH_SEGMENT)*"
    private val REMOTE_PATH = "$REMOTE_START_PATH/mainframer/$projectName"
    val FILE_PATH_REGEX = "(?:$REMOTE_PATH$END_PATH)".toRegex()
    val FIRST_FRAGMENT_REGEX = "(.*:\\s)".toRegex()
    val LINE_WITH_REMOTE_EXCEPTION = "${FIRST_FRAGMENT_REGEX.pattern}${FILE_PATH_REGEX.pattern}".toRegex()
    private val LINE_NUMBER_START = ":(?:\\s\\()?"
    private val LINE_NUMBER_VALUE = "(\\d+)"
    private val LINE_NUMBER_END = "(?:,\\s\\d+\\))?"
    val LINE_NUMBER_REGEX = "$LINE_NUMBER_START$LINE_NUMBER_VALUE$LINE_NUMBER_END".toRegex()
}

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
        assertRegexMatches(
                regexString = converter.FILE_PATH_REGEX,
                input = "/mainframer/$PROJECT_NAME")
    }

    @Test
    fun `Should not catch file path if it is not from remote machine`() {
        assertRegexNotMatches(
                regexString = converter.FILE_PATH_REGEX,
                input = "/$PROJECT_NAME")
    }

    @Test
    fun `Should catch file path if it starts with longer path`() {
        assertRegexMatches(
                regexString = converter.FILE_PATH_REGEX,
                input = "/longer/path/mainframer/$PROJECT_NAME"
        )
    }

    @Test
    fun `Should catch file path if it ends with kotlin class name`() {
        assertRegexMatches(
                regexString = converter.FILE_PATH_REGEX,
                input = "/longer/path/mainframer/$PROJECT_NAME/Example.kt"
        )
    }

    @Test
    fun `Should catch file path if it ends with java class name`() {
        assertRegexMatches(
                regexString = converter.FILE_PATH_REGEX,
                input = "/longer/path/mainframer/$PROJECT_NAME/Example.java"
        )
    }

    @Test
    fun `Should not catch file path if it ends with undefined class name`() {
        assertRegexNotMatches(
                regexString = converter.FILE_PATH_REGEX,
                input = "/longer/path/mainframer/$PROJECT_NAME/Example."
        )
    }

    @Test
    fun `Should replace remote base path with given local path`() {
        val replacedPath = "Error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (19, 10): error".replace(converter.FILE_PATH_REGEX.toRegex(), "$localBasePath<$1>")
        Assertions.assertThat(replacedPath).isEqualTo("Error: $localBasePath</com/elpassion/mainframer/Example.kt>: (19, 10): error")
    }

    @Test
    fun `Should replace remote base path with given local path and have first fragment`() {
        val replacedPath = "Complicated error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (19, 10): error: Errors everywhere!".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$localBasePath$1:$2")
        Assertions.assertThat(replacedPath).isEqualTo("Complicated error: $localBasePath/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!")
    }

    @Test
    fun `Should replace remote base path with given local path also when line number is simply given`() {
        val replacedPath = "Complicated error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$localBasePath$1:$2")
        Assertions.assertThat(replacedPath).isEqualTo("Complicated error: $localBasePath/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!")
    }

    @Test
    fun `Should catch line number`() {
        assertRegexMatches(
                regexString = converter.LINE_NUMBER_REGEX,
                input = ":100"
        )
    }

    @Test
    fun `Should not catch wrong line number`() {
        assertRegexNotMatches(
                regexString = converter.LINE_NUMBER_REGEX,
                input = ":wrongLineNumber"
        )
    }

    @Test
    fun `Should catch line number when there is also given column number`() {
        assertRegexMatches(
                regexString = converter.LINE_NUMBER_REGEX,
                input = ": (9, 10)"
        )
    }

    @Test
    fun `Should format correctly simple path line number value`() {
        val replacedPathSimple = ":321".replace(converter.LINE_NUMBER_REGEX.toRegex(), ":$1")
        Assertions.assertThat(replacedPathSimple).isEqualTo(":321")
    }

    @Test
    fun `Should format correctly complex path line number value`() {
        val replacedPathComplex = ": (90, 100)".replace(converter.LINE_NUMBER_REGEX.toRegex(), ":$1")
        Assertions.assertThat(replacedPathComplex).isEqualTo(":90")
    }

    @Test
    fun `Should catch path segment`() {
        assertRegexMatches(
                regexString = converter.PATH_SEGMENT,
                input = "/test/test2"
        )
    }

    private fun assertRegexMatches(regexString: String, input: String) {
        assertTrue(regexString.toRegex().matches(input))
    }

    private fun assertRegexNotMatches(regexString: String, input: String) {
        assertFalse(regexString.toRegex().matches(input))
    }

}

class RemoteToLocalInputConverter(projectName: String) {
    val PATH_SEGMENT = "(?:/\\w+)*?"
    private val FILE_EXTENSION = "\\.\\w+"
    private val END_PATH = "($PATH_SEGMENT$FILE_EXTENSION)*"
    private val REMOTE_START_PATH = "(?:$PATH_SEGMENT)*"
    private val REMOTE_PATH = "$REMOTE_START_PATH/mainframer/$projectName"
    val FILE_PATH_REGEX = "(?:$REMOTE_PATH$END_PATH)"
    private val LINE_NUMBER_START = ":(?:\\s\\()?"
    private val LINE_NUMBER_VALUE = "(\\d+)"
    private val LINE_NUMBER_END = "(?:,\\s\\d+\\))?"
    val LINE_NUMBER_REGEX = "$LINE_NUMBER_START$LINE_NUMBER_VALUE$LINE_NUMBER_END"
    val LINE_WITH_REMOTE_EXCEPTION = "$FILE_PATH_REGEX$LINE_NUMBER_REGEX".toRegex()
}

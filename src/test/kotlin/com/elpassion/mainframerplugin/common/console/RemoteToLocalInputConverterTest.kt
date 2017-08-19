package com.elpassion.mainframerplugin.common.console

import org.assertj.core.api.Assertions
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteToLocalInputConverterTest {

    private val PROJECT_NAME = "testProject"
    private val localBasePath = "/base/path/$PROJECT_NAME"
    private val converter = RemoteToLocalInputConverter(PROJECT_NAME, localBasePath)

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
    fun `Should catch path`() {
        assertRegexMatches(
                regexString = converter.PATH,
                input = "/test/test2"
        )
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
    fun `Should catch colon and space signs in first fragment of line`() {
        assertRegexMatches(
                regexString = converter.FIRST_FRAGMENT_REGEX,
                input = ": "
        )
    }

    @Test
    fun `Should catch whole first fragment of line`() {
        assertRegexMatches(
                regexString = converter.FIRST_FRAGMENT_REGEX,
                input = "Very complicated exception: "
        )
    }

    @Test
    fun `Should check only one line if it matches first fragment regex`() {
        assertRegexNotMatches(
                regexString = converter.FIRST_FRAGMENT_REGEX,
                input = "Very complicated exception\n: "
        )
    }

    @Test
    fun `Should replace first fragment only`() {
        val replacedPath = "Error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (19, 10): error".replaceFirst(converter.FIRST_FRAGMENT_REGEX.toRegex(), "<>")
        Assertions.assertThat(replacedPath).isEqualTo("<>/longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (19, 10): error")
    }

    @Test
    fun `Should replace remote base path with given local path`() {
        val replacedPath = "Error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (19, 10): error".replace(converter.FILE_PATH_REGEX.toRegex(), "$localBasePath<$1>")
        Assertions.assertThat(replacedPath).isEqualTo("Error: $localBasePath</com/elpassion/mainframer/Example.kt>: (19, 10): error")
    }

    @Test
    fun `Should replace remote base path with given local path when inside path is package with dots`() {
        val replacedPath = "Error: /longer/path/mainframer/$PROJECT_NAME/src/main/com.elpassion.mainframer/Example.java: (19, 10): error".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$localBasePath<$1>:$2")
        Assertions.assertThat(replacedPath).isEqualTo("$localBasePath</src/main/com.elpassion.mainframer/Example.java>:19: error")
    }

    @Test
    fun `Should replace remote base path with given local path and have first fragment`() {
        val replacedPath = "Complicated error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (19, 10): error: Errors everywhere!".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$localBasePath$1:$2")
        Assertions.assertThat(replacedPath).isEqualTo("$localBasePath/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!")
    }

    @Test
    fun `Should replace remote base path with given local path also when line number is simply given`() {
        val replacedPath = "Complicated error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$localBasePath$1:$2")
        Assertions.assertThat(replacedPath).isEqualTo("$localBasePath/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!")
    }

    @Test
    fun `Should replace remote base path with given local path also when first fragment is missing`() {
        val replacedPath = "/longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!".replace(converter.LINE_WITH_REMOTE_EXCEPTION, "$localBasePath$1:$2")
        Assertions.assertThat(replacedPath).isEqualTo("$localBasePath/com/elpassion/mainframer/Example.kt:19: error: Errors everywhere!")
    }

    @Test
    fun `Should replace remote base path and change line number when method convertInput is being used`() {
        val input = "Error: /longer/path/mainframer/$PROJECT_NAME/com/elpassion/mainframer/Example.kt: (1, 20): error: Errors everywhere!"
        val expectedConvertedInput = "$localBasePath/com/elpassion/mainframer/Example.kt:1: error: Errors everywhere!"
        val convertedInput = converter.convertInput(input)
        Assertions.assertThat(convertedInput).isEqualTo(expectedConvertedInput)
    }

    @Test(timeout = 1000)
    fun `Should not freeze on several long paths`() {
        val input = "-Dretrolambda.includedFiles=/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$observePropertyUpdateStatus\$2.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$PropertyUpdateStatus\$Failed.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$WithDefaultAndSetPrecondition.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$observePropertyUpdateStatus\$1.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$WithDefaultAndSetPrecondition\$1.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/screen/licenses/LicensesScreenActivity.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/screen/licenses/LicensesScreenPresenter.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$default\$1.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$defaultWithSetPrecondition\$1.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/activity/ActivityStateSaver.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/debug/DebugSettings.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/screen/home/HomeScreenActivity.class:/Users/alexey/Projects/tc-android-app-template/app/build/intermediates/classes/dev/debug/com/omnigon/template/storage/BasePropertyDelegate\$PropertyUpdateStatus\$Success.class\n"
        val convertedInput = converter.convertInput(input)
        Assertions.assertThat(convertedInput).isEqualTo(input)
    }

    @Test
    fun `Should replace remote path even if remote path contains dashes`() {
        val input = "/home/kasper-work/mainframer/$PROJECT_NAME/src/main/java/A.java:1"
        val expectedConvertedInput = "$localBasePath/src/main/java/A.java:1"
        val convertedInput = converter.convertInput(input)
        Assertions.assertThat(convertedInput).isEqualTo(expectedConvertedInput)
    }

    @Test
    fun `Should replace remote path even if package contains dashes`() {
        val input = "/home/mainframer/$PROJECT_NAME/src/main/com.elpassion-with-dashes/Example.java:1"
        val expectedConvertedInput = "$localBasePath/src/main/com.elpassion-with-dashes/Example.java:1"
        val convertedInput = converter.convertInput(input)
        Assertions.assertThat(convertedInput).isEqualTo(expectedConvertedInput)
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

    private fun assertRegexMatches(regexString: String, input: String) {
        assertTrue(regexString.toRegex().matches(input))
    }

    private fun assertRegexNotMatches(regexString: String, input: String) {
        assertFalse(regexString.toRegex().matches(input))
    }

}
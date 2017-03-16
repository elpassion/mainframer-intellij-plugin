package com.elpassion.intelijidea.common.console

import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteToLocalInputConverterTest {

    private val PROJECT_NAME = "testProject"

    @Test
    fun `Should catch file path from remote machine`() {
        val converter = RemoteToLocalInputConverter(PROJECT_NAME)
        assertTrue(converter.FILE_PATH_REGEX.matches("/mainframer/$PROJECT_NAME"))
    }

}

class RemoteToLocalInputConverter(private val projectName: String) {
    val FILE_PATH_REGEX = "(.+)".toRegex()
}

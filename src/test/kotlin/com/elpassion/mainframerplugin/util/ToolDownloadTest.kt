package com.elpassion.mainframerplugin.util

import com.elpassion.mainframerplugin.common.assertThrows
import org.hamcrest.core.StringEndsWith.endsWith
import org.junit.Assert.assertThat
import org.junit.Test

class ToolDownloadTest {

    @Test
    fun `Get download url ending with proper filename`() {
        assertThat(getToolDownloadUrl("3.0.0"), endsWith(toolFilename))
    }

    @Test
    fun `Get download url ending with proper filename for pre 3 version`() {
        assertThat(getToolDownloadUrl("2.0.0"), endsWith("$toolFilename.sh"))
    }

    @Test
    fun `Expect runtime exception on invalid version format`() {
        assertThrows<RuntimeException> {
            getToolDownloadUrl("invalid version")
        }
    }
}
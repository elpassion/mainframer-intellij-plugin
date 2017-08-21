package com.elpassion.mainframerplugin.util

import org.hamcrest.core.StringEndsWith.endsWith
import org.junit.Assert.assertThat
import org.junit.Test

class ToolDownloadTest {

    @Test
    fun `Get download url ending with proper filename`() {
        assertThat(getToolDownloadUrl("3.0.0"), endsWith(toolFilename))
    }
}
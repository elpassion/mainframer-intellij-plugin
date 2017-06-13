package com.elpassion.mainframerplugin.integration

import com.elpassion.mainframerplugin.action.configure.templater.resourceCopier
import com.intellij.testFramework.IdeaTestCase
import java.io.File

class ResourceCopierTest : IdeaTestCase() {

    fun testShouldCopyFileFromSourceToDestination() {
        val file = File("")
        val copyDestination = "${file.absolutePath}${File.separator}justAFile"
        resourceCopier("templates${File.separator}ignoreExample", copyDestination)
                .test()
                .assertNoErrors()
        val copiedFile = File(copyDestination).apply { deleteOnExit() }
        assertTrue(copiedFile.exists())
    }
}
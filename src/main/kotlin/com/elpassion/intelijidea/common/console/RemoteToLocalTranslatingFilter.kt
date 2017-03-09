package com.elpassion.intelijidea.common.console

import com.intellij.execution.filters.HyperlinkInfo
import com.intellij.execution.filters.OpenFileHyperlinkInfo
import com.intellij.execution.filters.RegexpFilter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

class RemoteToLocalTranslatingFilter(private val project: Project) : RegexpFilter(project, FILE_PATH_MACROS + ":" + LINE_MACROS) {

    override fun createOpenFileHyperlink(fileName: String, line: Int, column: Int): HyperlinkInfo? {
        val normalizedFileName = fileName.replace(File.separatorChar, '/')
        val localizedFileName = RemoteToLocalFileTranslator.translate(project, remoteFilePath = normalizedFileName)
        val file = LocalFileSystem.getInstance().findFileByPathIfCached(localizedFileName)
        return if (file != null) OpenFileHyperlinkInfo(project, file, line, column) else null
    }
}
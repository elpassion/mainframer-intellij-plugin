package com.elpassion.intelijidea.action.configure.downloader

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import com.intellij.platform.templates.github.Outcome
import java.io.File

object MFDownloader {

    fun downloadFileToProject(url: String, project: Project, outputFilename: String): Outcome<Unit> {
        val title = "Downloading file"
        val message = "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}..."
        val action = {
            val progressIndicator = ProgressManager.getInstance().progressIndicator
            DownloadUtil.downloadAtomically(progressIndicator, url, File(project.basePath, outputFilename))
            project.baseDir.refresh(true, false)
        }
        return DownloadUtil.provideDataWithProgressSynchronously(project, title, message, action, null)
    }
}
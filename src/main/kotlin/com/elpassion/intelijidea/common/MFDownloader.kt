package com.elpassion.intelijidea.common

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import java.io.File

object MFDownloader {

    fun downloadFileToProject(url: String, project: Project, outputFilename: String) {
        val title = "Downloading file"
        val message = "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}..."
        val action = {
            val progressIndicator = ProgressManager.getInstance().progressIndicator
            DownloadUtil.downloadAtomically(progressIndicator, url, File(project.basePath, outputFilename))
            project.baseDir.refresh(true, false)
        }
        DownloadUtil.provideDataWithProgressSynchronously(project, title, message, action, null)
    }
}
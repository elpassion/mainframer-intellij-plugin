package com.elpassion.intelijidea.common

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import java.io.File
import java.net.URL

object MFDownloader {

    fun downloadFileToProject(url: URL, project: Project, outputFilename: String) {
        DownloadUtil.provideDataWithProgressSynchronously(project,
                "Downloading file", "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}...", {
            val progressIndicator = ProgressManager.getInstance().progressIndicator
            DownloadUtil.downloadAtomically(progressIndicator, url.toString(), File(project.basePath, outputFilename))
            project.baseDir.refresh(true, false)
        }, null)
    }
}
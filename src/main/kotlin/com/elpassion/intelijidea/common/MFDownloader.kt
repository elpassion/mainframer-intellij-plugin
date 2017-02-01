package com.elpassion.intelijidea.common

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import java.io.File

object MFDownloader {

    fun downloadFileToProject(url: String, project: Project, outputFilename: String) {
        DownloadUtil.provideDataWithProgressSynchronously(project,
                "Downloading file", "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}...", {
            val progressIndicator = ProgressManager.getInstance().progressIndicator
            DownloadUtil.downloadAtomically(progressIndicator, url, File(project.basePath, outputFilename))
            project.baseDir.refresh(true, false)
        }, null)
    }
}
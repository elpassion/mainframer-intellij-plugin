package com.elpassion.intelijidea.action.configure.downloader

import com.elpassion.intelijidea.util.asResultObservable
import com.elpassion.intelijidea.util.getMfToolDownloadUrl
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import com.intellij.platform.templates.github.Outcome
import io.reactivex.Observable
import java.io.File

fun mfFileDownloader(project: Project) = { version: String ->
    downloadFileToProject(project, getMfToolDownloadUrl(version), mfFilename).asResultObservable()
}

//TODO: Make private and remove @Deprecated annotation
@Deprecated(message = "Replace with MFConfigureProjectAction")
fun downloadFileToProject(project: Project, url: String, outputFilename: String): Outcome<Unit> {
    val title = "Downloading file"
    val message = "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}..."
    val action = {
        val progressIndicator = ProgressManager.getInstance().progressIndicator
        DownloadUtil.downloadAtomically(progressIndicator, url, File(project.basePath, outputFilename))
        project.baseDir.refresh(true, false)
    }
    return DownloadUtil.provideDataWithProgressSynchronously(project, title, message, action, null)
}
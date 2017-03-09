package com.elpassion.intelijidea.action.configure.downloader

import com.elpassion.intelijidea.action.configure.configurator.MFToolInfo
import com.elpassion.intelijidea.util.asResultObservable
import com.elpassion.intelijidea.util.getMfToolDownloadUrl
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import com.intellij.platform.templates.github.Outcome
import io.reactivex.Maybe
import java.io.File

fun mfFileDownloader(project: Project): (MFToolInfo) -> Maybe<Unit> = { (version, file) ->
    downloadFileToProject(project, getMfToolDownloadUrl(version), file).asResultObservable()
}

private fun downloadFileToProject(project: Project, url: String, outputFile: File): Outcome<Unit> {
    val title = "Downloading file"
    val message = "Downloading ${DownloadUtil.CONTENT_LENGTH_TEMPLATE}..."
    val action = {
        val progressIndicator = ProgressManager.getInstance().progressIndicator
        DownloadUtil.downloadAtomically(progressIndicator, url, outputFile)
        outputFile.setExecutable(true)
        project.baseDir.refresh(true, false)
    }
    return DownloadUtil.provideDataWithProgressSynchronously(project, title, message, action, null)
}
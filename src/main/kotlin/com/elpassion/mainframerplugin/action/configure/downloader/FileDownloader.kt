package com.elpassion.mainframerplugin.action.configure.downloader

import com.elpassion.mainframerplugin.action.configure.configurator.ToolInfo
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.util.asResultObservable
import com.elpassion.mainframerplugin.util.getToolDownloadUrl
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.platform.templates.github.DownloadUtil
import com.intellij.platform.templates.github.Outcome
import io.reactivex.Maybe
import java.io.File

fun fileDownloader(project: Project): (ToolInfo) -> Maybe<File> = { (version, file) ->
    downloadFileToProject(project, getToolDownloadUrl(version), file).asResultObservable()
}

private fun downloadFileToProject(project: Project, url: String, outputFile: File): Outcome<File> {
    val title = StringsBundle.getMessage("configure.downloader.title")
    val message = StringsBundle.getMessage("configure.downloader.text", DownloadUtil.CONTENT_LENGTH_TEMPLATE)
    return DownloadUtil.provideDataWithProgressSynchronously(project, title, message, fileSupplier(outputFile, project, url), null)
}

private fun fileSupplier(outputFile: File, project: Project, url: String): () -> File = {
    val progressIndicator = ProgressManager.getInstance().progressIndicator
    DownloadUtil.downloadAtomically(progressIndicator, url, outputFile)
    project.baseDir.refresh(true, false)
    outputFile
}
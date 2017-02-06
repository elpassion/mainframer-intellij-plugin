package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.releases.MFConfigureProjectDialog
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubApi
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubRetrofit
import com.elpassion.intelijidea.action.configure.releases.service.MFVersionsReleaseService
import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.getMfToolDownloadUrl
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.platform.templates.github.Outcome

class MFConfigureProjectAction : AnAction(MF_CONFIGURE_PROJECT) {
    private val service = MFVersionsReleaseService(provideGithubApi(provideGithubRetrofit()))

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.configureMainframer()
    }

    private fun Project.configureMainframer() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(this, "Downloading mainframer versions") {
            override fun run(indicator: ProgressIndicator) {
                service.getVersions()
                        .observeOn(UIScheduler)
                        .subscribe({
                            showMFConfigureDialog(it)
                        }, {
                            Messages.showInfoMessage(it.message, MF_CONFIGURE_PROJECT)
                        })
            }
        })
    }

    private fun Project.showMFConfigureDialog(versionsList: List<String>) {
        MFConfigureProjectDialog(this, versionsList) { version ->
            val outcome = downloadMainframer(version)
            Messages.showInfoMessage(outcome.getMessage(), MF_CONFIGURE_PROJECT)
        }.show()
    }

    private fun Outcome<Unit>.getMessage() = when {
        isCancelled -> "Mainframer configuration canceled"
        exception != null -> "Error during mainframer configuration"
        else -> "Mainframer configured in your project!"
    }

    private fun Project.downloadMainframer(version: String) =
            MFDownloader.downloadFileToProject(getMfToolDownloadUrl(version), this, mfFilename)

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }
}
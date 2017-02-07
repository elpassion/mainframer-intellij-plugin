package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.chooser.MFVersionChooser
import com.elpassion.intelijidea.action.configure.downloader.MFFileDownloader
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubApi
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubRetrofit
import com.elpassion.intelijidea.action.configure.releases.service.MFReleasesFetcher
import com.elpassion.intelijidea.common.ProgressScheduler
import com.elpassion.intelijidea.common.UIScheduler
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MFConfigureProjectAction : AnAction(MF_CONFIGURE_PROJECT) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            MFConfigureProjectActionController(
                    mainframerReleasesFetcher = MFReleasesFetcher(provideGithubApi(provideGithubRetrofit())),
                    mainframerVersionChooser = MFVersionChooser(project),
                    mainframerFileDownloader = MFFileDownloader(project),
                    showMessage = { message -> Messages.showInfoMessage(message, MF_CONFIGURE_PROJECT) },
                    uiScheduler = UIScheduler,
                    progressScheduler = ProgressScheduler(project, "Downloading mainframer versions")
            ).configureMainframer()
        }
    }

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }
}

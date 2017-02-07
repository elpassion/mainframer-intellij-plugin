package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.chooser.MFConfigureProjectDialog
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubApi
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubRetrofit
import com.elpassion.intelijidea.action.configure.releases.service.MFVersionsReleaseService
import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.common.ProgressScheduler
import com.elpassion.intelijidea.common.UIScheduler
import com.elpassion.intelijidea.util.asResultObservable
import com.elpassion.intelijidea.util.getMfToolDownloadUrl
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MFConfigureProjectAction : AnAction(MF_CONFIGURE_PROJECT) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            MFConfigureProjectActionController(
                    releaseService = MFVersionsReleaseService(provideGithubApi(provideGithubRetrofit())),
                    versionChooser = { versionsList ->
                        MFConfigureProjectDialog(project, versionsList).showAsObservable()
                    },
                    downloadMainframer = { version ->
                        MFDownloader.downloadFileToProject(getMfToolDownloadUrl(version), project, mfFilename).asResultObservable()
                    },
                    showMessage = { message ->
                        Messages.showInfoMessage(message, MF_CONFIGURE_PROJECT)
                    },
                    uiScheduler = UIScheduler,
                    progressScheduler = ProgressScheduler(project, "Downloading mainframer versions")).configureMainframer()
        }
    }

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }
}

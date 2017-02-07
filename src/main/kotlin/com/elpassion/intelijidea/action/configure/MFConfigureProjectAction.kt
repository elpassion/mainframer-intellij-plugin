package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.releases.MFConfigureProjectDialog
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubApi
import com.elpassion.intelijidea.action.configure.releases.api.provideGithubRetrofit
import com.elpassion.intelijidea.action.configure.releases.service.MFVersionsReleaseService
import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.common.Result
import com.elpassion.intelijidea.util.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.platform.templates.github.Outcome
import io.reactivex.Observable
import io.reactivex.Scheduler

class MFConfigureProjectAction : AnAction(MF_CONFIGURE_PROJECT) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            MFConfigureProjectActionController(
                    service = MFVersionsReleaseService(provideGithubApi(provideGithubRetrofit())),
                    versionChooser = { versionsList ->
                        MFConfigureProjectDialog(project, versionsList).showAsObservable()
                    },
                    downloadMainframer = { version ->
                        MFDownloader.downloadFileToProject(getMfToolDownloadUrl(version), project, mfFilename).asResultObservable()
                    },
                    progressScheduler = ProgressScheduler(project, "Downloading mainframer versions")).configureMainframer()
        }
    }

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }

    class MFConfigureProjectActionController(
            val service: MFVersionsReleaseService,
            val versionChooser: (List<String>) -> Observable<Result<String>>,
            val downloadMainframer: (String) -> Observable<Result<Unit>>,
            val progressScheduler: Scheduler) {

        fun configureMainframer() {
            service.getVersions()
                    .subscribeOn(progressScheduler)
                    .observeOn(UIScheduler)
                    .flatMap(versionChooser)
                    .flatMapResult(downloadMainframer)
                    .subscribeResult({
                        Messages.showInfoMessage("Mainframer configured in your project!", MFConfigureProjectAction.MF_CONFIGURE_PROJECT)
                    }, {
                        Messages.showInfoMessage("Mainframer configuration canceled", MFConfigureProjectAction.MF_CONFIGURE_PROJECT)
                    }, {
                        Messages.showInfoMessage("Error during mainframer configuration", MFConfigureProjectAction.MF_CONFIGURE_PROJECT)
                    })
        }
    }
}

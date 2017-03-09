package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.configurator.mfConfigurationDialog
import com.elpassion.intelijidea.action.configure.configurator.mfConfigurator
import com.elpassion.intelijidea.action.configure.downloader.mfFileDownloader
import com.elpassion.intelijidea.action.configure.releases.api.provideGitHubApi
import com.elpassion.intelijidea.action.configure.releases.api.provideGitHubRetrofit
import com.elpassion.intelijidea.action.configure.releases.service.mfReleasesFetcher
import com.elpassion.intelijidea.common.ProgressScheduler
import com.elpassion.intelijidea.common.UIScheduler
import com.elpassion.intelijidea.util.showError
import com.elpassion.intelijidea.util.showInfo
import com.intellij.openapi.project.Project

fun configureMFToProject(project: Project) =
        MFConfigureProjectActionController(
                mainframerReleasesFetcher = mfReleasesFetcher(provideGitHubApi(provideGitHubRetrofit())),
                mainframerConfigurator = mfConfigurator(project, mfConfigurationDialog(project)),
                mainframerFileDownloader = mfFileDownloader(project),
                showMessage = { message -> showInfo(project, message) },
                showError = { message -> showError(project, message) },
                uiScheduler = UIScheduler,
                progressScheduler = ProgressScheduler(project, "Downloading mainframer versions")).configureMainframer()
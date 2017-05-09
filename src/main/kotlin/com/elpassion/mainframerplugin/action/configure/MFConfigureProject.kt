package com.elpassion.mainframerplugin.action.configure

import com.elpassion.mainframerplugin.action.configure.configurator.mfConfigurationDialog
import com.elpassion.mainframerplugin.action.configure.configurator.mfConfigurator
import com.elpassion.mainframerplugin.action.configure.downloader.mfFileDownloader
import com.elpassion.mainframerplugin.action.configure.releases.api.provideGitHubApi
import com.elpassion.mainframerplugin.action.configure.releases.api.provideGitHubRetrofit
import com.elpassion.mainframerplugin.action.configure.releases.service.mfReleasesFetcher
import com.elpassion.mainframerplugin.common.ProgressScheduler
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.UIScheduler
import com.elpassion.mainframerplugin.util.showError
import com.elpassion.mainframerplugin.util.showInfo
import com.intellij.openapi.project.Project

fun configureMFToProject(project: Project) =
        MFConfigureProjectActionController(
                mainframerReleasesFetcher = mfReleasesFetcher(provideGitHubApi(provideGitHubRetrofit())),
                mainframerConfigurator = mfConfigurator(project, mfConfigurationDialog(project)),
                mainframerFileDownloader = mfFileDownloader(project),
                showMessage = { message -> showInfo(project, message) },
                showError = { message -> showError(project, message) },
                uiScheduler = UIScheduler,
                progressScheduler = ProgressScheduler(project, StringsBundle.getMessage("configure.fetching.versions.description"))).configureMainframer()

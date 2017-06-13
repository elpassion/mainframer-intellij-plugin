package com.elpassion.mainframerplugin.action.configure

import com.elpassion.mainframerplugin.action.configure.configurator.configurationDialog
import com.elpassion.mainframerplugin.action.configure.configurator.configurator
import com.elpassion.mainframerplugin.action.configure.downloader.fileDownloader
import com.elpassion.mainframerplugin.action.configure.releases.api.provideGitHubApi
import com.elpassion.mainframerplugin.action.configure.releases.api.provideGitHubRetrofit
import com.elpassion.mainframerplugin.action.configure.releases.service.releasesFetcher
import com.elpassion.mainframerplugin.action.configure.templater.resourceCopier
import com.elpassion.mainframerplugin.action.configure.templater.templateChooser
import com.elpassion.mainframerplugin.action.configure.templater.templateSetter
import com.elpassion.mainframerplugin.common.ProgressScheduler
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.UIScheduler
import com.elpassion.mainframerplugin.util.showError
import com.elpassion.mainframerplugin.util.showInfo
import com.intellij.openapi.project.Project

fun configureToolInProject(project: Project) =
        ConfigureProjectActionController(
                releasesFetcher = releasesFetcher(provideGitHubApi(provideGitHubRetrofit())),
                configurator = configurator(project, configurationDialog(project)),
                fileDownloader = fileDownloader(project),
                showMessage = { message -> showInfo(project, message) },
                showError = { message -> showError(project, message) },
                uiScheduler = UIScheduler,
                progressScheduler = ProgressScheduler(project, StringsBundle.getMessage("configure.fetching.versions.description")),
                templateChooser = { templateChooser(project) },
                templateFileResolver = templateSetter(project),
                fileCopier = resourceCopier).configureMainframer()

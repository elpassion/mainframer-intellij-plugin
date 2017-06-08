package com.elpassion.mainframerplugin.action.configure

import com.elpassion.mainframerplugin.action.configure.configurator.ToolInfo
import com.elpassion.mainframerplugin.action.configure.templater.ProjectType
import com.elpassion.mainframerplugin.common.StringsBundle
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import java.io.File

class ConfigureProjectActionController(
        val releasesFetcher: () -> Single<List<String>>,
        val configurator: (List<String>) -> Maybe<ToolInfo>,
        val fileDownloader: (ToolInfo) -> Maybe<File>,
        val showMessage: (String) -> Unit,
        val showError: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler,
        val templater: () -> Maybe<ProjectType>) {

    fun configureMainframer() {
        releasesFetcher()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMapMaybe(configurator)
                .flatMap(fileDownloader)
                .doOnSuccess { it.setExecutable(true) }
                .flatMap { templater() }
                .subscribe({
                    showMessage(StringsBundle.getMessage("configure.success"))
                }, {
                    showError(StringsBundle.getMessage("configure.error"))
                })
    }
}

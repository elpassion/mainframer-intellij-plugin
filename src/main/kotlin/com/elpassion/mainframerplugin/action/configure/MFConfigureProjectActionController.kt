package com.elpassion.mainframerplugin.action.configure

import com.elpassion.mainframerplugin.action.configure.configurator.MFToolInfo
import com.elpassion.mainframerplugin.common.StringsBundle
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import java.io.File

class MFConfigureProjectActionController(
        val mainframerReleasesFetcher: () -> Single<List<String>>,
        val mainframerConfigurator: (List<String>) -> Maybe<MFToolInfo>,
        val mainframerFileDownloader: (MFToolInfo) -> Maybe<File>,
        val showMessage: (String) -> Unit,
        val showError: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler) {

    fun configureMainframer() {
        mainframerReleasesFetcher()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMapMaybe(mainframerConfigurator)
                .flatMap(mainframerFileDownloader)
                .doOnSuccess { it.setExecutable(true) }
                .subscribe({
                    showMessage(StringsBundle.getMessage("configure.success"))
                }, {
                    showError(StringsBundle.getMessage("configure.error"))
                })
    }
}

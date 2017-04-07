package com.elpassion.mainframerplugin.action.configure

import com.elpassion.mainframerplugin.action.configure.configurator.MFToolInfo
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
                    showMessage("Mainframer configured in your project!")
                }, {
                    showError("Error during Mainframer configuration")
                })
    }
}

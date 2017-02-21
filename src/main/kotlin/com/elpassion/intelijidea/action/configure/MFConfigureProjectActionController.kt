package com.elpassion.intelijidea.action.configure

import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import java.io.File

class MFConfigureProjectActionController(
        val mainframerReleasesFetcher: () -> Single<List<String>>,
        val mainframerConfigurator: (List<String>) -> Maybe<Pair<String, File>>,
        val mainframerFileDownloader: (String, File) -> Maybe<Unit>,
        val showMessage: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler) {

    fun configureMainframer() {
        mainframerReleasesFetcher()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMapMaybe(mainframerConfigurator)
                .flatMap { mainframerFileDownloader(it.first, it.second) }
                .subscribe({
                    showMessage("Mainframer configured in your project!")
                }, {
                    showMessage("Error during mainframer configuration")
                })
    }
}
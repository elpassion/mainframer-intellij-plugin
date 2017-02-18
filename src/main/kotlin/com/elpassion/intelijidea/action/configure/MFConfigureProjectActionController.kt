package com.elpassion.intelijidea.action.configure

import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single

class MFConfigureProjectActionController(
        val mainframerReleasesFetcher: () -> Single<List<String>>,
        val mainframerConfigurator: (List<String>) -> Maybe<String>,
        val mainframerFileDownloader: (String) -> Maybe<Unit>,
        val showMessage: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler) {

    fun configureMainframer() {
        mainframerReleasesFetcher()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMapMaybe(mainframerConfigurator)
                .flatMap(mainframerFileDownloader)
                .subscribe({
                    showMessage("Mainframer configured in your project!")
                }, {
                    showMessage("Error during mainframer configuration")
                })
    }
}
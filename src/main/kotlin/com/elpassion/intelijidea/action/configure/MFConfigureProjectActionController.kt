package com.elpassion.intelijidea.action.configure

import io.reactivex.Observable
import io.reactivex.Scheduler

class MFConfigureProjectActionController(
        val mainframerReleasesFetcher: () -> Observable<List<String>>,
        val mainframerConfigurator: (List<String>) -> Observable<String>,
        val mainframerFileDownloader: (String) -> Observable<Unit>,
        val showMessage: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler) {

    fun configureMainframer() {
        mainframerReleasesFetcher()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMap(mainframerConfigurator)
                .flatMap(mainframerFileDownloader)
                .subscribe({
                    showMessage("Mainframer configured in your project!")
                }, {
                    showMessage("Error during mainframer configuration")
                })
    }
}
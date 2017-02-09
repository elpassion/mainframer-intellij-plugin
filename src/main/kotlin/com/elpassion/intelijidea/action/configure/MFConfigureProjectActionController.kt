package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.common.Result
import com.elpassion.intelijidea.util.flatMapResult
import com.elpassion.intelijidea.util.subscribeResult
import io.reactivex.Observable
import io.reactivex.Scheduler

class MFConfigureProjectActionController(
        val mainframerReleasesFetcher: () -> Observable<List<String>>,
        val mainframerVersionChooser: (List<String>) -> Observable<Result<String>>,
        val mainframerFileDownloader: (String) -> Observable<Result<Unit>>,
        val showMessage: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler) {

    fun configureMainframer() {
        mainframerReleasesFetcher.invoke()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMap(mainframerVersionChooser)
                .flatMapResult(mainframerFileDownloader)
                .subscribeResult(
                        onSuccess = {
                            showMessage("Mainframer configured in your project!")
                        },
                        onCancelled = {

                        },
                        onError = {
                            showMessage("Error during mainframer configuration")
                        })
    }
}
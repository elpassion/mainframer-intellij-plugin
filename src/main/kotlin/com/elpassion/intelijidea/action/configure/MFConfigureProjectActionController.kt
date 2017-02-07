package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.releases.service.MFVersionsReleaseService
import com.elpassion.intelijidea.common.Result
import com.elpassion.intelijidea.util.flatMapResult
import com.elpassion.intelijidea.util.subscribeResult
import io.reactivex.Observable
import io.reactivex.Scheduler

class MFConfigureProjectActionController(
        val service: MFVersionsReleaseService,
        val versionChooser: (List<String>) -> Observable<Result<String>>,
        val downloadMainframer: (String) -> Observable<Result<Unit>>,
        val showMessage: (String) -> Unit,
        val uiScheduler: Scheduler,
        val progressScheduler: Scheduler) {

    fun configureMainframer() {
        service.getVersions()
                .subscribeOn(progressScheduler)
                .observeOn(uiScheduler)
                .flatMap(versionChooser)
                .flatMapResult(downloadMainframer)
                .subscribeResult({
                    showMessage("Mainframer configured in your project!")
                }, {
                    showMessage("Mainframer configuration canceled")
                }, {
                    showMessage("Error during mainframer configuration")
                })
    }
}
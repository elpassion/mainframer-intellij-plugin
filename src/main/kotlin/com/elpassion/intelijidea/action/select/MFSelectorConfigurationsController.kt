package com.elpassion.intelijidea.action.select

import com.elpassion.intelijidea.action.configure.selector.MFSelectorResult
import io.reactivex.Maybe

class MFSelectorConfigurationsController(val manipulateTasks: (MFSelectorResult) -> Unit,
                                         val selectorResult:()-> Maybe<MFSelectorResult>,
                                         val isSettingsTaskValid: () -> Boolean,
                                         val showMessage: (Int, Int) -> Unit,
                                         val showError: (String) -> Unit) {
    fun configure() {
        selectorResult().flatMap {
            if (!it.toInject.isEmpty() && !isSettingsTaskValid()) {
                Maybe.error(RuntimeException("Invalid inject configuration"))
            } else {
                Maybe.just(it)
            }
        }.doOnSuccess {
            manipulateTasks(it)
        }.map {
            it.toInject.size to it.toRestore.size
        }.subscribe({ (injected, restored) ->
            showMessage(injected, restored)
        }, {
            showError("MF is not configured, \n" +
                    "<a href=\"\">click here</a> to change default settings")
        })
    }


}

package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.action.configure.selector.MFSelectorResult
import com.elpassion.mainframerplugin.common.StringsBundle
import io.reactivex.Maybe

class MFSelectorConfigurationsController(val manipulateTasks: (MFSelectorResult) -> Unit,
                                         val selectorResult: () -> Maybe<MFSelectorResult>,
                                         val isSettingsTaskValid: () -> Boolean,
                                         val showMessage: (Int, Int) -> Unit,
                                         val showError: (String) -> Unit) {
    fun configure() {
        selectorResult().map {
            if (!it.toInject.isEmpty() && !isSettingsTaskValid()) {
                throw RuntimeException(StringsBundle.getMessage("selector.invalid.configuration.error"))
            } else {
                it
            }
        }.doOnSuccess {
            manipulateTasks(it)
        }.map {
            it.toInject.size to it.toRestore.size
        }.subscribe({ (injected, restored) ->
            showMessage(injected, restored)
        }, {
            showError(StringsBundle.getMessage("selector.mainframer.not.configured.error"))
        })
    }


}

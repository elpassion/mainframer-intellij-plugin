package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.action.configure.selector.SelectorResult
import com.elpassion.mainframerplugin.common.StringsBundle
import io.reactivex.Maybe

class SelectorConfigurationsController(val manipulateTasks: (SelectorResult) -> Unit,
                                       val selectorResult: () -> Maybe<SelectorResult>,
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

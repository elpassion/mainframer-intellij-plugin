package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.action.configure.selector.SelectorResult
import com.elpassion.mainframerplugin.common.StringsBundle
import io.reactivex.Maybe

class SelectorConfigurationsController(val manipulateTasks: (SelectorResult) -> Unit,
                                       val selectorResult: () -> Maybe<SelectorResult>,
                                       val isSettingsTaskValid: () -> Boolean,
                                       val showMessage: (Int, Int) -> Unit,
                                       val showMainframerNotConfiguredError: (String) -> Unit,
                                       val showMainframerTaskInvalidError: (String) -> Unit,
                                       val doesMainframerExists: () -> Boolean) {
    fun configure() {
        selectorResult().map {
            assertSystemState(it)
            it
        }.doOnSuccess {
            manipulateTasks(it)
        }.map {
            it.toInject.size to it.toRestore.size
        }.subscribe({ (injected, restored) ->
            showMessage(injected, restored)
        }, {
            when (it) {
                is MainframerNotConfiguredException -> showMainframerNotConfiguredError(it.message!!)
                is InvalidTaskConfigurationException -> showMainframerTaskInvalidError(it.message!!)
            }
        })
    }

    private fun assertSystemState(it: SelectorResult) {
        when {
            !it.toInject.isEmpty() -> when {
                !isSettingsTaskValid() -> throw InvalidTaskConfigurationException(StringsBundle.getMessage("selector.invalid.configuration.error"))
                !doesMainframerExists() -> throw MainframerNotConfiguredException(StringsBundle.getMessage("selector.mainframer.not.configured.error"))
            }
        }
    }
}

private class InvalidTaskConfigurationException(message: String) : RuntimeException(message)

private class MainframerNotConfiguredException(message: String) : RuntimeException(message)
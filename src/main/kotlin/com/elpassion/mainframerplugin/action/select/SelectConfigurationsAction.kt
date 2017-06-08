package com.elpassion.mainframerplugin.action.select

import com.elpassion.mainframerplugin.common.StringsBundle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SelectConfigurationsAction : AnAction(SELECT_CONFIGURATIONS_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let(::selectConfigurationsToMFActions)
    }

    companion object {
        private val SELECT_CONFIGURATIONS_ACTION = StringsBundle.getMessage("selector.action.name")
    }
}

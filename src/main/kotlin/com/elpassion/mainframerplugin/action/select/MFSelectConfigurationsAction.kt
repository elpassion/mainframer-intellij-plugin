package com.elpassion.mainframerplugin.action.select

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFSelectConfigurationsAction : AnAction(MF_SELECT_CONFIGURATIONS_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let(::selectConfigurationsToMFActions)
    }

    companion object {
        private val MF_SELECT_CONFIGURATIONS_ACTION = "Select configurations to inject mainframer or restore to default"
    }
}
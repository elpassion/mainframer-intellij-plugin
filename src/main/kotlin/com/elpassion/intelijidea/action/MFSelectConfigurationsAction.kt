package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.action.configure.selector.mfSelector
import com.elpassion.intelijidea.action.configure.selector.showSelectorDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFSelectConfigurationsAction : AnAction(MF_SELECT_CONFIGURATIONS_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            mfSelector(project) { items ->
                showSelectorDialog(project, items)
            }.subscribe()
        }
    }

    companion object {
        private val MF_SELECT_CONFIGURATIONS_ACTION = "Select configurations to inject mainframer (incubating feature)"
    }
}
package com.elpassion.intelijidea.action.switcher

import com.elpassion.intelijidea.common.MFStateProvider
import com.elpassion.intelijidea.util.MFIcons
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFSwitchAction : AnAction(MF_SWTICH_ACTION) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            MFStateProvider.getInstance(it).apply {
                isTurnOn = !isTurnOn
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.project?.let {
            e.presentation.icon = if(MFStateProvider.getInstance(it).isTurnOn) MFIcons.mainframerIcon else AllIcons.Windows.CloseActive
        }
    }

    companion object {
        private val MF_SWTICH_ACTION = "Quickly turn on/off mainframer"
    }
}
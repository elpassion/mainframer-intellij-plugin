package com.elpassion.mainframerplugin.action.switcher

import com.elpassion.mainframerplugin.common.MFStateProvider
import com.elpassion.mainframerplugin.util.MFIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MFSwitchAction : AnAction(MF_SWITCH_ACTION) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            MFStateProvider.getInstance(it).apply {
                isTurnOn = !isTurnOn
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.project?.let {
            e.presentation.icon = if (MFStateProvider.getInstance(it).isTurnOn) MFIcons.disableMainframerIcon else MFIcons.enableMainframerIcon
        }
    }

    companion object {
        private val MF_SWITCH_ACTION = "Quickly turn on/off Mainframer"
    }
}

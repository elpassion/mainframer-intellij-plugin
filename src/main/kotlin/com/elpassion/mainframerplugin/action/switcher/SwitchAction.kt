package com.elpassion.mainframerplugin.action.switcher

import com.elpassion.mainframerplugin.common.StateProvider
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.util.MainframerIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SwitchAction : AnAction(SWITCH_ACTION) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let {
            StateProvider.getInstance(it).apply {
                isTurnOn = !isTurnOn
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.project?.let {
            e.presentation.icon = if (StateProvider.getInstance(it).isTurnOn) MainframerIcons.disableMainframerIcon else MainframerIcons.enableMainframerIcon
        }
    }

    companion object {
        private val SWITCH_ACTION = StringsBundle.getMessage("switcher.action.name")
    }
}

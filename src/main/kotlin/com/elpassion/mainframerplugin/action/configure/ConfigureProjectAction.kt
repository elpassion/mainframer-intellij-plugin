package com.elpassion.mainframerplugin.action.configure

import com.elpassion.mainframerplugin.common.StringsBundle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ConfigureProjectAction : AnAction(CONFIGURE_PROJECT) {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let(::configureToolInProject)
    }

    companion object {
        private val CONFIGURE_PROJECT = StringsBundle.getMessage("configure.action.name")
    }
}

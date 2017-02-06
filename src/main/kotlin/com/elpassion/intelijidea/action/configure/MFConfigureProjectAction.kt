package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.releases.MFConfigureProjectDialog
import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.getMfToolDownloadUrl
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.platform.templates.github.Outcome

class MFConfigureProjectAction : AnAction(MF_CONFIGURE_PROJECT) {

    override fun actionPerformed(event: AnActionEvent?) {
        event?.project?.configureMainframer()
    }

    private fun Project.configureMainframer() {
        MFConfigureProjectDialog(this) { version ->
            val outcome = downloadMainframer(version)
            Messages.showInfoMessage(outcome.getMessage(), MF_CONFIGURE_PROJECT)
        }.show()
    }

    private fun Outcome<Unit>.getMessage() = when {
        isCancelled -> "Mainframer configuration canceled"
        exception != null -> "Error during mainframer configuration"
        else -> "Mainframer configured in your project!"
    }

    private fun Project.downloadMainframer(version: String) =
            MFDownloader.downloadFileToProject(getMfToolDownloadUrl(version), this, mfFilename)

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }
}
package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.getMfToolDownloadUrl
import com.elpassion.intelijidea.util.hasChild
import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class MFConfigureProjectAction : AnAction(MF_CONFIGURE_PROJECT) {

    override fun actionPerformed(event: AnActionEvent?) {
        event?.project?.configureMainframer()
    }

    private fun Project.configureMainframer() {
        if (!baseDir.hasChild(mfFilename)) {
            MFConfigureProjectDialog(this) { version ->
                downloadMainframer(version)
            }.show()
        }
        Messages.showInfoMessage("Mainframer configured in your project!", MF_CONFIGURE_PROJECT)
    }

    private fun Project.downloadMainframer(version: String) {
        MFDownloader.downloadFileToProject(getMfToolDownloadUrl(version), this, mfFilename)
    }

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }
}
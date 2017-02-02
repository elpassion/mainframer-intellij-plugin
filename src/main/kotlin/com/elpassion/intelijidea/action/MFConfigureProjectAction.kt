package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.hasChild
import com.elpassion.intelijidea.util.mfFilename
import com.elpassion.intelijidea.util.mfScriptDownloadUrl
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
            MFConfigureProjectDialog(this).show()
            MFDownloader.downloadFileToProject(mfScriptDownloadUrl, this, mfFilename)
        }
        Messages.showInfoMessage("Mainframer configured in your project!", MF_CONFIGURE_PROJECT)
    }

    companion object {
        private val MF_CONFIGURE_PROJECT = "Configure Mainframer in Project"
    }
}
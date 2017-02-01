package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.common.MFDownloader
import com.elpassion.intelijidea.util.hasChild
import com.elpassion.intelijidea.util.mfFilename
import com.elpassion.intelijidea.util.mfScriptDownloadUrl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class MFConfigureProjectAction : AnAction("Configure Mainframer in Project") {

    override fun actionPerformed(event: AnActionEvent?) {
        event?.project?.let { configureMainframerInProject(it) }
    }

    private fun configureMainframerInProject(project: Project) {
        if (!project.baseDir.hasChild(mfFilename)) {
            MFDownloader.downloadFileToProject(mfScriptDownloadUrl, project, mfFilename)
        }
    }
}
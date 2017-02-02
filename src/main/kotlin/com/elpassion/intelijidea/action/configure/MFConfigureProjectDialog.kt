package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.ui.MFConfigureProjectForm
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class MFConfigureProjectDialog(project: Project) : DialogWrapper(project, false) {

    private val form = MFConfigureProjectForm()

    init {
        title = "Configure Mainframer in Project"
        init()
        //TODO: populate combo box with available mf versions
    }

    override fun createCenterPanel(): JComponent = form.panel
}
package com.elpassion.intelijidea.action.configure.releases

import com.elpassion.intelijidea.action.configure.releases.ui.MFConfigureProjectForm
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent


class MFConfigureProjectDialog(project: Project, val releaseVersionsList: List<String>, private val onVersion: (String) -> Unit) : DialogWrapper(project, false) {

    private val form = MFConfigureProjectForm()

    init {
        title = "Configure Mainframer in Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.versionComboBox.model = MFComboBoxViewModel(releaseVersionsList)
        return form.panel
    }

    override fun doOKAction() {
        super.doOKAction()
        val selectedVersion = form.versionComboBox.selectedItem.toString()
        onVersion(selectedVersion)
    }
}

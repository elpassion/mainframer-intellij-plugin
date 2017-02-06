package com.elpassion.intelijidea.action.configure

import com.elpassion.intelijidea.action.configure.ui.MFConfigureProjectForm
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class MFConfigureProjectDialog(project: Project, val onVersion: (String) -> Unit) : DialogWrapper(project, false) {

    private val form = MFConfigureProjectForm()
    private val service = MFVersionsReleaseService()
    
    init {
        title = "Configure Mainframer in Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.versionComboBox.model = MFComboBoxViewModel(service.getVersions())
        return form.panel
    }

    override fun doOKAction() {
        super.doOKAction()
        val selectedVersion = form.versionComboBox.selectedItem.toString()
        onVersion(selectedVersion)
    }
}

package com.elpassion.intelijidea.action.configure.chooser

import com.elpassion.intelijidea.action.configure.chooser.ui.MFVersionChooserForm
import com.elpassion.intelijidea.common.RxDialogWrapper
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class MFVersionChooserDialog(project: Project, val releaseVersionsList: List<String>) : RxDialogWrapper<String>(project) {

    private val form = MFVersionChooserForm()

    init {
        title = "Configure Mainframer in Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.versionComboBox.model = MFVersionChooserViewModel(releaseVersionsList)
        return form.panel
    }

    override fun doValidate(): ValidationInfo? {
        form.taskNameField.run {
            if (text.isEmpty()) {
                return ValidationInfo("Task name cannot be empty!", this)
            }
        }
        form.buildCommandField.run {
            if (text.isEmpty()) {
                return ValidationInfo("Build command cannot be empty!", this)
            }
        }
        return null
    }
    override fun getSuccessResult() = form.versionComboBox.selectedItem.toString()
}

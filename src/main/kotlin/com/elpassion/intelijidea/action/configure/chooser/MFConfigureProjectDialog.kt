package com.elpassion.intelijidea.action.configure.chooser

import com.elpassion.intelijidea.action.configure.chooser.ui.MFConfigureProjectForm
import com.elpassion.intelijidea.common.RxDialogWrapper
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.functions.Cancellable
import javax.swing.JComponent

class MFConfigureProjectDialog(project: Project, val releaseVersionsList: List<String>) : RxDialogWrapper<String>(project) {

    private val form = MFConfigureProjectForm()

    init {
        title = "Configure Mainframer in Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.versionComboBox.model = MFComboBoxViewModel(releaseVersionsList)
        return form.panel
    }

    override fun getSuccessResult() = form.versionComboBox.selectedItem.toString()
}

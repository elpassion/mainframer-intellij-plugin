package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.action.configure.configurator.ui.MConfiguratorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
import com.elpassion.intelijidea.common.BuildCommandValidator
import com.elpassion.intelijidea.common.RemoteMachineFieldValidator
import com.elpassion.intelijidea.common.TaskFieldValidator
import com.elpassion.intelijidea.common.validateForm
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import javax.swing.JComponent

class MFConfiguratorDialog(project: Project,
                           val releaseVersionsList: List<String>,
                           val defaultValues: MFConfiguratorIn,
                           doOnOk: (MFConfiguratorOut) -> Unit,
                           doOnCancel: () -> Unit) : DialogWrapperAdapter<MFConfiguratorOut>(project, doOnOk, doOnCancel) {

    private val form = MConfiguratorForm()

    init {
        title = "Configure Mainframer in Project"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.versionComboBox.model = CollectionComboBoxModel(releaseVersionsList)
        form.buildCommandField.text = defaultValues.buildCommand
        form.taskNameField.text = defaultValues.taskName
        form.remoteMachineField.text = defaultValues.remoteName
        return form.panel
    }

    override fun doValidate() = with(form) {
        validateForm(RemoteMachineFieldValidator(remoteMachineField), BuildCommandValidator(buildCommandField), TaskFieldValidator(taskNameField))
    }

    override fun getSuccessResult() = MFConfiguratorOut(
            version = form.versionComboBox.selectedItem.toString(),
            buildCommand = form.buildCommandField.text,
            taskName = form.taskNameField.text,
            remoteMachine = form.remoteMachineField.text)
}

package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.action.configure.configurator.ui.MConfiguratorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
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
        form.versionComboBox.model = MFVersionChooserViewModel(releaseVersionsList)
        form.buildCommandField.text = defaultValues.buildCommand
        form.taskNameField.text = defaultValues.taskName
        form.remoteMachineField.text = defaultValues.remoteName
        return form.panel
    }

    override fun doValidate(): ValidationInfo? {
        form.buildCommandField.run {
            if (text.isEmpty()) {
                return ValidationInfo("Build command cannot be empty!", this)
            }
        }
        form.taskNameField.run {
            if (text.isEmpty()) {
                return ValidationInfo("Task name cannot be empty!", this)
            }
        }
        form.remoteMachineField.run {
            if (text.isEmpty()) {
                return ValidationInfo("Task name cannot be empty!", this)
            }
        }
        return null
    }

    override fun getSuccessResult() = MFConfiguratorOut(
            version = form.versionComboBox.selectedItem.toString(),
            buildCommand = form.buildCommandField.text,
            taskName = form.taskNameField.text,
            remoteMachine = form.remoteMachineField.text)
}

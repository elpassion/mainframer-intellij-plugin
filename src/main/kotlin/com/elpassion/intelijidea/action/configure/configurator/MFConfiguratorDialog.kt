package com.elpassion.intelijidea.action.configure.configurator

import com.elpassion.intelijidea.action.configure.configurator.ui.MConfiguratorForm
import com.elpassion.intelijidea.common.RxDialogWrapper
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import javax.swing.JComponent

class MFConfiguratorDialog(project: Project, val releaseVersionsList: List<String>) : RxDialogWrapper<MFConfiguratorViewModel>(project) {

    private val form = MConfiguratorForm()

    init {
        title = "Configure Mainframer in Project"
        init()
    }

    fun applyDefaultValues(taskData: MFTaskData): MFConfiguratorDialog {
        with(taskData){
            form.taskNameField.text = taskName
            form.buildCommandField.text = buildCommand
        }
        return this
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

    override fun getSuccessResult() = MFConfiguratorViewModel(
            version = form.versionComboBox.selectedItem.toString(),
            buildCommand = form.buildCommandField.text,
            taskName = form.taskNameField.text)
}

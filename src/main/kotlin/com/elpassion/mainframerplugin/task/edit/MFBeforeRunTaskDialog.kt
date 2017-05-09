package com.elpassion.mainframerplugin.task.edit

import com.elpassion.mainframerplugin.common.BuildCommandValidator
import com.elpassion.mainframerplugin.common.MainframerPathValidator
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.validateForm
import com.elpassion.mainframerplugin.task.MFTaskData
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class MFBeforeRunTaskDialog(project: Project) : DialogWrapper(project) {

    val form = MFBeforeRunTaskForm(project)

    init {
        isModal = true
        title = StringsBundle.getMessage("beforeRunTask.title")
        init()
    }

    override fun createCenterPanel(): JComponent = form.panel

    override fun doValidate() = with(form) {
        validateForm(MainframerPathValidator(mainframerToolField), BuildCommandValidator(buildCommandField))
    }

    fun restoreMainframerTaskData(data: MFTaskData) {
        form.mainframerToolField.text = data.mainframerPath
        form.buildCommandField.text = data.buildCommand
    }

    fun createMFTaskDataFromForms() = MFTaskData(
            buildCommand = form.buildCommandField.text,
            mainframerPath = form.mainframerToolField.text)
}
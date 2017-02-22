package com.elpassion.intelijidea.task.edit

import com.elpassion.intelijidea.common.BuildCommandValidator
import com.elpassion.intelijidea.common.MainframerPathValidator
import com.elpassion.intelijidea.common.TaskFieldValidator
import com.elpassion.intelijidea.common.validateForm
import com.elpassion.intelijidea.task.MFTaskData
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class MFBeforeRunTaskDialog(project: Project) : DialogWrapper(project) {

    val form = MFBeforeRunTaskForm(project)

    init {
        isModal = true
        init()
    }

    override fun createCenterPanel(): JComponent = form.panel

    override fun doValidate() = with(form) {
        validateForm(MainframerPathValidator(mainframerToolField), BuildCommandValidator(buildCommandField), TaskFieldValidator(taskField))
    }

    fun restoreMainframerTaskData(data: MFTaskData) {
        form.mainframerToolField.text = data.mainframerPath ?: ""
        form.buildCommandField.text = data.buildCommand
        form.taskField.text = data.taskName
    }

    fun createMFTaskDataFromForms() = MFTaskData(
            mainframerPath = form.mainframerToolField.text,
            buildCommand = form.buildCommandField.text,
            taskName = form.taskField.text)
}
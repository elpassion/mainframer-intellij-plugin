package com.elpassion.intelijidea.task.edit

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
        validateTaskFormFields(taskField, buildCommandField, mainframerToolField)
    }

    fun restoreMainframerTaskData(data: MFTaskData) {
        form.mainframerToolField.setText(data.mainframerPath)
        form.buildCommandField.text = data.buildCommand
        form.taskField.text = data.taskName
    }

    fun createMFTaskDataFromForms() = MFTaskData(
            mainframerPath = form.mainframerToolField.text.trim(),
            buildCommand = form.buildCommandField.text.trim(),
            taskName = form.taskField.text.trim())
}
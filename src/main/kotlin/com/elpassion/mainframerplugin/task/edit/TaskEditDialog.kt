package com.elpassion.mainframerplugin.task.edit

import com.elpassion.mainframerplugin.common.BuildCommandValidator
import com.elpassion.mainframerplugin.common.MainframerPathValidator
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.common.validateForm
import com.elpassion.mainframerplugin.task.TaskData
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class TaskEditDialog(project: Project) : DialogWrapper(project) {

    val form = TaskEditForm(project)

    init {
        isModal = true
        title = StringsBundle.getMessage("task.settings.dialog.title")
        init()
    }

    override fun createCenterPanel(): JComponent = form.panel

    override fun doValidate() = with(form) {
        validateForm(MainframerPathValidator(mainframerToolField), BuildCommandValidator(buildCommandField))
    }

    fun restoreTaskData(data: TaskData) {
        form.mainframerToolField.text = data.mainframerPath
        form.buildCommandField.text = data.buildCommand
    }

    fun createTaskDataFromForms() = TaskData(
            buildCommand = form.buildCommandField.text,
            mainframerPath = form.mainframerToolField.text)
}
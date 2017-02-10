package com.elpassion.intelijidea.task.edit

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import javax.swing.JTextField

class TaskEditValidator( val form: TaskEditForm) {

    fun doValidate() =
            when {
                taskFieldIsEmpty() -> ValidationInfo("Task cannot be empty", form.taskField())
                buildCommandIsEmpty() -> ValidationInfo("Build command cannot be empty", form.buildCommandField())
                pathIsEmpty() -> ValidationInfo("Path cannot be empty", form.mainframerToolField())
                else -> null
            }

    private fun taskFieldIsEmpty(): Boolean = form.taskField().text.isBlank()

    private fun buildCommandIsEmpty(): Boolean = form.buildCommandField().text.isBlank()

    private fun pathIsEmpty(): Boolean = form.taskField().text.isBlank()

}

interface TaskEditForm {
    fun taskField(): JTextField
    fun buildCommandField(): JTextField
    fun mainframerToolField(): TextFieldWithBrowseButton

}

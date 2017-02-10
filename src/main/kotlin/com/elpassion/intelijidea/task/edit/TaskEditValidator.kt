package com.elpassion.intelijidea.task.edit

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import java.io.File
import javax.swing.JTextField

class TaskEditValidator(val form: TaskEditForm) {

    fun doValidate() =
            when {
                taskFieldIsEmpty() -> ValidationInfo("Task cannot be empty", form.taskField())
                buildCommandIsEmpty() -> ValidationInfo("Build command cannot be empty", form.buildCommandField())
                pathIsEmpty() -> ValidationInfo("Path cannot be empty", form.mainframerToolField())
                pathIsInvalid() -> ValidationInfo("Cannot found mainframer script in path", form.mainframerToolField())
                else -> null
            }

    private fun pathIsInvalid() = !File(form.mainframerToolField().text, mfFilename).exists()

    private fun taskFieldIsEmpty() = form.taskField().text.isBlank()

    private fun buildCommandIsEmpty() = form.buildCommandField().text.isBlank()

    private fun pathIsEmpty() = form.mainframerToolField().text.isBlank()

}

interface TaskEditForm {
    fun taskField(): JTextField
    fun buildCommandField(): JTextField
    fun mainframerToolField(): TextFieldWithBrowseButton

}

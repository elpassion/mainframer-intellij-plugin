package com.elpassion.intelijidea.common

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.util.containers.stream
import java.io.File
import javax.swing.JTextField

fun validateForm(vararg validators: FieldValidator): ValidationInfo? = validators.map { it.validate() }.firstOrNull { it != null }

interface FieldValidator {
    fun validate(): ValidationInfo?
}

class TaskFieldValidator(private val taskField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(taskField, "Task cannot be empty")
}

class BuildCommandValidator(private val buildCommandField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(buildCommandField, "Build command cannot be empty")
}

class MainframerPathValidator(private val mainframerPathField: TextFieldWithBrowseButton) : FieldValidator {
    override fun validate() =
            when {
                mainframerPathField.isBlank() -> ValidationInfo("Path cannot be empty", mainframerPathField)
                mainframerPathField.isPathToScriptInvalid() -> ValidationInfo("Cannot find mainframer script in path", mainframerPathField)
                mainframerPathField.isScriptNotExecutable() -> ValidationInfo("Mainframer script in not executable", mainframerPathField)
                else -> null
            }
}

class RemoteMachineFieldValidator(private val remoteMachineField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(jTextField = remoteMachineField, message = "Remote machine name cannot be empty!")
}

private fun validateEmpty(jTextField: JTextField, message: String) = if (jTextField.isBlank()) ValidationInfo(message, jTextField) else null

private fun TextFieldWithBrowseButton.isScriptNotExecutable() = File(text).canExecute().not()

private fun TextFieldWithBrowseButton.isPathToScriptInvalid() = File(text).exists().not()

private fun TextFieldWithBrowseButton.isBlank() = text.isBlank()

private fun JTextField.isBlank() = text.isBlank()
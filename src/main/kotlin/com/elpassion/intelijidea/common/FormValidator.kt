package com.elpassion.intelijidea.common

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import java.io.File
import javax.swing.JTextField

fun validateForm(vararg validators: FieldValidator): ValidationInfo? = validators.map { it.validate() }.firstOrNull { it != null }

interface FieldValidator {
    fun validate(): ValidationInfo?
}

class BuildCommandValidator(private val buildCommandField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(buildCommandField, "Build command cannot be empty")
}

class MainframerPathValidator(private val mainframerPathField: TextFieldWithBrowseButton) : FieldValidator {
    override fun validate() =
            with(mainframerPathField) {
                when {
                    isBlank() -> ValidationInfo("Path cannot be empty", this)
                    isPathToScriptInvalid() -> ValidationInfo("Cannot find mainframer script in path", this)
                    isScriptNotExecutable() -> ValidationInfo("Mainframer script in not executable", this)
                    else -> null
                }
            }
}

class RemoteMachineFieldValidator(private val remoteMachineField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(jTextField = remoteMachineField, message = "Remote machine name cannot be empty!")
}

private fun validateEmpty(jTextField: JTextField, message: String) = if (jTextField.isBlank()) ValidationInfo(message, jTextField) else null

private fun TextFieldWithBrowseButton.isScriptNotExecutable() = File(text).canExecute().not()

private fun TextFieldWithBrowseButton.isPathToScriptInvalid() = File(text).let { !it.exists() || !it.isFile }

private fun TextFieldWithBrowseButton.isBlank() = text.isBlank()

private fun JTextField.isBlank() = text.isBlank()
package com.elpassion.mainframerplugin.common

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import java.io.File
import javax.swing.JTextField

fun validateForm(vararg validators: FieldValidator): ValidationInfo? = validators.map { it.validate() }.firstOrNull { it != null }

interface FieldValidator {
    fun validate(): ValidationInfo?
}

class BuildCommandValidator(private val buildCommandField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(buildCommandField, StringsBundle.getMessage("common.form.validator.build.command.empty"))
}

class MainframerPathValidator(private val mainframerPathField: TextFieldWithBrowseButton) : FieldValidator {
    override fun validate() = with(mainframerPathField) {
        when {
            isBlank() -> ValidationInfo(StringsBundle.getMessage("common.form.validator.path.empty"), this)
            isPathToScriptInvalid() -> ValidationInfo(StringsBundle.getMessage("common.form.validator.script.not.found"), this)
            isScriptNotExecutable() -> ValidationInfo(StringsBundle.getMessage("common.form.validator.script.not.executable"), this)
            else -> null
        }
    }
}

class RemoteMachineFieldValidator(private val remoteMachineField: JTextField) : FieldValidator {
    override fun validate() = validateEmpty(jTextField = remoteMachineField, message = StringsBundle.getMessage("common.form.validator.remote.name.empty"))
}

private fun validateEmpty(jTextField: JTextField, message: String) = if (jTextField.isBlank()) ValidationInfo(message, jTextField) else null

private fun TextFieldWithBrowseButton.isScriptNotExecutable() = File(text).canExecute().not()

private fun TextFieldWithBrowseButton.isPathToScriptInvalid() = File(text).let { !it.exists() || !it.isFile }

private fun TextFieldWithBrowseButton.isBlank() = text.isBlank()

private fun JTextField.isBlank() = text.isBlank()

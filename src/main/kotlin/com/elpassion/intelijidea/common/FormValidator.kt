package com.elpassion.intelijidea.common

import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import java.io.File
import javax.swing.JTextField

fun validateForm(vararg validators: FieldValidator): ValidationInfo? = validators.map { it.validate() }.firstOrNull { it != null }

interface FieldValidator {
    fun validate(): ValidationInfo?
}

class TaskFieldValidator(private val taskField: JTextField) : FieldValidator {
    override fun validate(): ValidationInfo? {
        return if (taskField.isBlank()) ValidationInfo("Task cannot be empty", taskField) else null
    }
}

class BuildCommandValidator(private val buildCommandField: JTextField) : FieldValidator {
    override fun validate(): ValidationInfo? {
        return if (buildCommandField.isBlank()) ValidationInfo("Build command cannot be empty", buildCommandField) else null
    }
}

class MainframerPathValidator(private val mainframerPathField: TextFieldWithBrowseButton) : FieldValidator {
    override fun validate(): ValidationInfo? {
        return if (mainframerPathField.isBlank()) ValidationInfo("Path cannot be empty", mainframerPathField)
        else if (mainframerPathField.isPathToScriptInvalid()) ValidationInfo("Cannot find mainframer script in path", mainframerPathField)
        else null
    }
}

class RemoteMachineFieldValidator(private val remoteMachineField: JTextField) : FieldValidator {
    override fun validate(): ValidationInfo? {
        return if (remoteMachineField.text.isEmpty()) ValidationInfo("Remote machine name cannot be empty!", remoteMachineField) else null
    }
}

private fun TextFieldWithBrowseButton.isPathToScriptInvalid() = File(text).exists().not()

private fun TextFieldWithBrowseButton.isBlank() = text.isBlank()

private fun JTextField.isBlank() = text.isBlank()
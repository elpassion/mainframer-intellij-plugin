package com.elpassion.intelijidea.task.edit

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import java.io.File
import javax.swing.JTextField

fun taskEditFormFormValidate(taskField: JTextField, buildCommandField: JTextField, mfFolderBrowser: TextFieldWithBrowseButton) =
        when {
            taskField.isBlank() -> ValidationInfo("Task cannot be empty", taskField)
            buildCommandField.isBlank() -> ValidationInfo("Build command cannot be empty", buildCommandField)
            mfFolderBrowser.isBlank() -> ValidationInfo("Path cannot be empty", mfFolderBrowser)
            mfFolderBrowser.isPathToScriptInvalid() -> ValidationInfo("Cannot find mainframer script in path", mfFolderBrowser)
            else -> null
        }

private fun TextFieldWithBrowseButton.isPathToScriptInvalid() = File(text, mfFilename).exists().not()

private fun TextFieldWithBrowseButton.isBlank() = text.isBlank()

private fun JTextField.isBlank() = text.isBlank()
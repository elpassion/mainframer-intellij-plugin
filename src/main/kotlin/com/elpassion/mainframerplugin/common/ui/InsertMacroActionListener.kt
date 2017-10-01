package com.elpassion.mainframerplugin.common.ui

import com.intellij.ide.macro.MacrosDialog
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFocusManager
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JTextField
import javax.swing.text.BadLocationException

class InsertMacroActionListener(private val myTextField: JTextField,
                                        private val project: Project) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {
        val dialog = MacrosDialog(project)
        if (dialog.showAndGet() && dialog.selectedMacro != null) {
            val macro = dialog.selectedMacro.name
            val position = myTextField.caretPosition
            try {
                myTextField.document.insertString(position, "$$macro$", null)
                myTextField.caretPosition = position + macro.length + 2
            } catch (ignored: BadLocationException) {
            }

        }
        IdeFocusManager.findInstance().requestFocus(myTextField, true)
    }
}
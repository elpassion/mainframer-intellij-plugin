package com.elpassion.intelijidea

import com.intellij.openapi.options.SettingsEditor
import javax.swing.JComponent


class MFSettingsEditor : SettingsEditor<MFRunnerConfiguration>() {

    override fun createEditor(): JComponent {
        return MFSettingsEditorPanel().mainPanel
    }

    override fun applyEditorTo(s: MFRunnerConfiguration) {

    }

    override fun resetEditorFrom(s: MFRunnerConfiguration) {

    }
}
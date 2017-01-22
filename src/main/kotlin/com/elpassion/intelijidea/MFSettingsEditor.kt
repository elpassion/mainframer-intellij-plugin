package com.elpassion.intelijidea

import com.intellij.openapi.options.SettingsEditor
import javax.swing.JComponent


class MFSettingsEditor : SettingsEditor<MFRunConfiguration>() {

    override fun createEditor(): JComponent {
        return MFDemoSettingsEditorPanel().mainPanel
    }

    override fun applyEditorTo(s: MFRunConfiguration) {

    }

    override fun resetEditorFrom(s: MFRunConfiguration) {

    }
}
package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.task.ui.MFBeforeTasDefaultSettingsPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

class MFBeforeTaskDefaultOptionsConfigurable(private val project: Project) : SearchableConfigurable, Configurable.NoScroll, Disposable {

    var mfSettingsPanel: MFBeforeTasDefaultSettingsPanel? = null

    override fun isModified(): Boolean {
        return mfSettingsPanel?.isModified ?: false
    }

    override fun getId(): String = "mainframer"

    override fun getDisplayName(): String = "Mainframer"

    override fun apply() {
        mfSettingsPanel?.apply()
    }

    override fun reset() {
        mfSettingsPanel?.reset()
    }

    override fun createComponent(): JComponent {
        mfSettingsPanel = MFBeforeTasDefaultSettingsPanel(project, MFBeforeTaskDefaultSettingsProvider.INSTANCE)
        return mfSettingsPanel!!.panel
    }

    override fun getHelpTopic(): String? = null

    override fun dispose() {
        mfSettingsPanel = null
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }
}
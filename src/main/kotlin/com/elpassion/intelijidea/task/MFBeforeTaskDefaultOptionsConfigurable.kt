package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.common.MFToolConfigurationImpl
import com.elpassion.intelijidea.task.ui.MFBeforeTaskDefaultSettingsPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

class MFBeforeTaskDefaultOptionsConfigurable(private val project: Project) : SearchableConfigurable, Configurable.NoScroll, Disposable {

    var mfSettingsPanel: MFBeforeTaskDefaultSettingsPanel? = null

    override fun isModified(): Boolean {
        return mfSettingsPanel?.isModified ?: false
    }

    override fun getId() = ID

    override fun getDisplayName() = DISPLAY_NAME

    override fun apply() {
        mfSettingsPanel?.apply()
    }

    override fun reset() {
        mfSettingsPanel?.reset()
    }

    override fun createComponent(): JComponent {
        mfSettingsPanel = MFBeforeTaskDefaultSettingsPanel(project, MFBeforeTaskDefaultSettingsProvider.getInstance(project), MFToolConfigurationImpl(project.basePath))
        return mfSettingsPanel!!.panel
    }

    override fun getHelpTopic(): String? = null

    override fun dispose() {
        mfSettingsPanel = null
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }

    //Android Studio 2.3 crash when this method return null
    override fun enableSearch(option: String?) = Runnable { }

    companion object {
        val ID = "mainframer"
        val DISPLAY_NAME = "Mainframer"
    }
}
package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.task.edit.taskFormValidate
import com.elpassion.intelijidea.task.ui.MFBeforeTaskDefaultSettingsPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

class MFBeforeTaskDefaultOptionsConfigurable(private val project: Project) : SearchableConfigurable, Configurable.NoScroll, Disposable {

    var mfSettingsPanel: MFBeforeTaskDefaultSettingsPanel? = null

    override fun isModified(): Boolean {
        return mfSettingsPanel?.isModified ?: false
    }

    override fun getId(): String = "mainframer"

    override fun getDisplayName(): String = "Mainframer"

    override fun apply() {
        with(mfSettingsPanel ?: return) {
            taskFormValidate(taskNameField, buildCommandField, mainframerToolField)?.let { throw ConfigurationException(it.message) }
            save()
        }
    }

    override fun reset() {
        mfSettingsPanel?.reset()
    }

    override fun createComponent(): JComponent {
        mfSettingsPanel = MFBeforeTaskDefaultSettingsPanel(project, MFBeforeTaskDefaultSettingsProvider.INSTANCE)
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
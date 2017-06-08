package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.common.ToolConfigurationImpl
import com.elpassion.mainframerplugin.common.StringsBundle
import com.elpassion.mainframerplugin.task.ui.TaskDefaultSettingsPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

class TaskDefaultOptionsConfigurable(private val project: Project) : SearchableConfigurable, Configurable.NoScroll, Disposable {

    var settingsPanel: TaskDefaultSettingsPanel? = null

    override fun isModified(): Boolean {
        return settingsPanel?.isModified ?: false
    }

    override fun getId() = ID

    override fun getDisplayName() = DISPLAY_NAME

    override fun apply() {
        settingsPanel?.apply()
    }

    override fun reset() {
        settingsPanel?.reset()
    }

    override fun createComponent(): JComponent {
        settingsPanel = TaskDefaultSettingsPanel(project, MainframerTaskDefaultSettingsProvider.getInstance(project), ToolConfigurationImpl(project.basePath))
        return settingsPanel!!.panel
    }

    override fun getHelpTopic(): String? = null

    override fun dispose() {
        settingsPanel = null
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }

    //Android Studio 2.3 crash when this method return null
    override fun enableSearch(option: String?) = Runnable { }

    companion object {
        val ID = "mainframer"
        val DISPLAY_NAME = StringsBundle.getMessage("taskEdit.default.provider.displayName")
    }
}
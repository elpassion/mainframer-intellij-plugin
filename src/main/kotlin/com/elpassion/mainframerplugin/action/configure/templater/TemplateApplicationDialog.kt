package com.elpassion.mainframerplugin.action.configure.templater

import com.elpassion.mainframerplugin.action.configure.templater.ui.TemplateChooser
import com.elpassion.mainframerplugin.common.DialogWrapperAdapter
import com.elpassion.mainframerplugin.common.StringsBundle
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionComboBoxModel
import io.reactivex.Maybe
import javax.swing.JComponent

class TemplateApplicationDialog(project: Project,
                                doOnOk: (ProjectType) -> Unit,
                                doOnCancel: () -> Unit) : DialogWrapperAdapter<ProjectType>(project, doOnOk, doOnCancel) {

    private val form = TemplateChooser()

    init {
        title = StringsBundle.getMessage("configure.templater.dialog.title")
        init()
    }

    override fun createCenterPanel(): JComponent? {
        form.projectTypeComboBox.model = CollectionComboBoxModel(ProjectType.values().map { it.displayName })
        return form.panel
    }

    override fun getSuccessResult(): ProjectType {
        return ProjectType.values()[form.projectTypeComboBox.selectedIndex]
    }
}

fun templateApplicationDialog(project: Project) =
        Maybe.create<ProjectType> { emitter ->
            TemplateApplicationDialog(project, {
                emitter.onSuccess(it)
            }, {
                emitter.onComplete()
            }).show()
        }


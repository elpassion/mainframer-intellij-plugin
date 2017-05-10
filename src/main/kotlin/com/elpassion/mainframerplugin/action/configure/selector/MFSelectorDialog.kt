package com.elpassion.mainframerplugin.action.configure.selector

import com.elpassion.mainframerplugin.action.configure.selector.ui.MFSelectorForm
import com.elpassion.mainframerplugin.common.DialogWrapperAdapter
import com.elpassion.mainframerplugin.common.StringsBundle
import com.intellij.openapi.project.Project
import io.reactivex.Maybe
import javax.swing.JComponent

class MFSelectorDialog(project: Project,
                       configurations: List<MFSelectorItem>,
                       templates: List<MFSelectorItem>,
                       doOnOk: (MFSelectorResult) -> Unit,
                       doOnCancel: () -> Unit) : DialogWrapperAdapter<MFSelectorResult>(project, doOnOk, doOnCancel) {

    private val form = MFSelectorForm()
    private val sortedConfigurations = configurations.sortedBy { it.name }
    private val sortedTemplateItems = templates.sortedBy { it.name }

    init {
        title = StringsBundle.getMessage("selector.dialog.title")
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.configurationItems.items = sortedConfigurations
        form.templateItems.items = sortedTemplateItems
        form.selectAllItems.addActionListener { form.configurationItems.selectAll() }
        form.unselectAllItems.addActionListener { form.configurationItems.unselectAll() }
        form.selectAllTemplateItems.addActionListener { form.templateItems.selectAll() }
        form.unselectAllTemplateItems.addActionListener { form.templateItems.unselectAll() }
        return form.panel
    }

    override fun getSuccessResult(): MFSelectorResult = getSelectorResult(
            uiIn = sortedConfigurations + sortedTemplateItems,
            uiOut = form.configurationItems.items + form.templateItems.items,
            replaceAll = form.replaceAll.isSelected)
}

fun showSelectorDialog(project: Project, configurations: List<MFSelectorItem>, templates: List<MFSelectorItem>): Maybe<MFSelectorResult> =
        Maybe.create<MFSelectorResult> { emitter ->
            MFSelectorDialog(project, configurations, templates, {
                emitter.onSuccess(it)
            }, {
                emitter.onComplete()
            }).show()
        }

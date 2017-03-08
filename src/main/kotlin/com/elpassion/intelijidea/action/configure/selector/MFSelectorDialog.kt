package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.configure.selector.ui.MFSelectorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
import com.intellij.openapi.project.Project
import com.jgoodies.common.collect.ArrayListModel
import io.reactivex.Maybe
import javax.swing.JCheckBox
import javax.swing.JComponent

class MFSelectorDialog(project: Project,
                       configurations: List<MFSelectorItem>,
                       templates: List<MFSelectorItem>,
                       doOnOk: (MFSelectorResult) -> Unit,
                       doOnCancel: () -> Unit) : DialogWrapperAdapter<MFSelectorResult>(project, doOnOk, doOnCancel) {

    private val form = MFSelectorForm()
    private val sortedConfigurations = configurations.sortedBy { it.getName() }
    private val sortedTemplateItems = templates.sortedBy { it.getName() }

    init {
        title = "Select run configurations to inject mainframer"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.configurationItems.model = sortedConfigurations.asListModel()
        form.templateItems.model = sortedTemplateItems.asListModel()
        form.selectAllItems.addActionListener { form.configurationItems.selectAll() }
        form.unselectAllItems.addActionListener { form.configurationItems.unselectAll() }
        form.selectAllTemplateItems.addActionListener { form.templateItems.selectAll() }
        form.unselectAllTemplateItems.addActionListener { form.templateItems.unselectAll() }
        return form.panel
    }

    override fun getSuccessResult(): MFSelectorResult = getSelectorResult(
            uiIn = sortedConfigurations + sortedTemplateItems,
            uiOut = sortedConfigurations.mapWithIndex { value.toItemFromUi(index) } + sortedTemplateItems.mapWithIndex { value.toTemplateItemFromUi(index) },
            replaceAll = form.replaceAll.isSelected)

    private fun List<MFSelectorItem>.asListModel() = ArrayListModel(map { createCheckBox(it) })

    private fun createCheckBox(item: MFSelectorItem) = JCheckBox(item.getName()).apply { isSelected = item.isSelected }

    private fun MFSelectorItem.toItemFromUi(index: Int) = copy(isSelected = form.configurationItems.isItemSelected(index))

    private fun MFSelectorItem.toTemplateItemFromUi(index: Int) = copy(isSelected = form.templateItems.isItemSelected(index))
}

private fun List<MFSelectorItem>.mapWithIndex(function: IndexedValue<MFSelectorItem>.() -> MFSelectorItem) = withIndex().map { function(it) }

fun showSelectorDialog(project: Project, configurations: List<MFSelectorItem>, templates: List<MFSelectorItem>): Maybe<MFSelectorResult> =
        Maybe.create<MFSelectorResult> { emitter ->
            MFSelectorDialog(project, configurations, templates, {
                emitter.onSuccess(it)
            }, {
                emitter.onComplete()
            }).show()
        }
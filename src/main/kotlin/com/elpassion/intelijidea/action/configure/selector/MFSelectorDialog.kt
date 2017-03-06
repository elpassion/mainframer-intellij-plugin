package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.configure.selector.ui.MFSelectorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
import com.elpassion.intelijidea.util.indexOfOrNull
import com.intellij.openapi.project.Project
import com.jgoodies.common.collect.ArrayListModel
import io.reactivex.Maybe
import javax.swing.JCheckBox
import javax.swing.JComponent

class MFSelectorDialog(project: Project,
                       val items: List<MFSelectorItem>,
                       doOnOk: (MFSelectorResult) -> Unit,
                       doOnCancel: () -> Unit) : DialogWrapperAdapter<MFSelectorResult>(project, doOnOk, doOnCancel) {

    private val form = MFSelectorForm()
    private val sortedItems = items.filterNot { it.isTemplate }.sortedBy { it.getName() }
    private val sortedTemplateItems = items.filter { it.isTemplate }.sortedBy { it.getName() }

    init {
        title = "Select run configurations to inject mainframer"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.items.model = sortedItems.asListModel()
        form.templateItems.model = sortedTemplateItems.asListModel()
        return form.panel
    }

    override fun getSuccessResult(): MFSelectorResult =
            getSelectorResult(uiIn = items, uiOut = items.map { it.toItemFromUi() })

    private fun List<MFSelectorItem>.asListModel() = ArrayListModel(map { createCheckBox(it) })

    private fun createCheckBox(item: MFSelectorItem) = JCheckBox(item.getName()).apply { isSelected = item.isSelected }

    private fun MFSelectorItem.toItemFromUi(): MFSelectorItem {
        val isSelected = sortedItems.indexOfOrNull(this)?.let { form.items.isItemSelected(it) } ?:
                sortedTemplateItems.indexOfOrNull(this)!!.let { form.templateItems.isItemSelected(it) }
        return MFSelectorItem(configuration, isTemplate, isSelected)
    }
}

fun showSelectorDialog(project: Project, selectorItems: List<MFSelectorItem>): Maybe<MFSelectorResult> =
        Maybe.create<MFSelectorResult> { emitter ->
            MFSelectorDialog(project, selectorItems, {
                emitter.onSuccess(it)
            }, {
                emitter.onComplete()
            }).show()
        }
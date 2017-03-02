package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.configure.selector.ui.MFSelectorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
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

    init {
        title = "Select run configurations to inject mainframer"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.items.model = items.filterNot { it.isTemplate }.asListModel()
        form.templateItems.model = items.filter { it.isTemplate }.asListModel()
        return form.panel
    }

    override fun getSuccessResult(): MFSelectorResult = getSelectorResult(uiIn = items, uiOut = items.mapFromUi())

    private fun List<MFSelectorItem>.asListModel() = ArrayListModel(sortedBy { it.getName() }.map { it.asCheckBox() })

    private fun MFSelectorItem.asCheckBox() = JCheckBox(getName()).apply {
        val item = this@asCheckBox
        isSelected = item.isSelected
    }

    private fun List<MFSelectorItem>.mapFromUi() = map {
        MFSelectorItem(it.configuration, it.isTemplate, form.items.isItemSelected(it))
    }
}

fun showSelectorDialog(project: Project, selectorItems: List<MFSelectorItem>): Maybe<MFSelectorResult> =
        Maybe.create<MFSelectorResult> { emitter ->
            MFSelectorDialog(project, selectorItems, {
                emitter.onSuccess(it)
                emitter.onComplete()
            }, {
                emitter.onComplete()
            }).show()
        }
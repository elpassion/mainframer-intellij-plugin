package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.configure.selector.ui.MFSelectorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
import com.intellij.openapi.project.Project
import com.jgoodies.common.collect.ArrayListModel
import javax.swing.JCheckBox
import javax.swing.JComponent

class MFSelectorDialog(project: Project,
                       val items: List<MFSelectorItem>,
                       doOnOk: (List<MFSelectorItem>) -> Unit,
                       doOnCancel: () -> Unit) : DialogWrapperAdapter<List<MFSelectorItem>>(project, doOnOk, doOnCancel) {

    private val form = MFSelectorForm()

    init {
        title = "Select run configurations to inject mainframer"
        init()
    }

    override fun createCenterPanel(): JComponent {
        form.items.model = items.filterNot { it.isTemplate }.asModel()
        form.templateItems.model = items.filter { it.isTemplate }.asModel()
        return form.panel
    }

    override fun getSuccessResult(): List<MFSelectorItem> = items

    private fun List<MFSelectorItem>.asModel() = ArrayListModel(map { it.asCheckBox() })

    private fun MFSelectorItem.asCheckBox() = JCheckBox(getName()).apply {
        val item = this@asCheckBox
        isSelected = item.isSelected
    }
}
package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.configure.selector.ui.MFSelectorCellRenderer
import com.elpassion.intelijidea.action.configure.selector.ui.MFSelectorForm
import com.elpassion.intelijidea.common.DialogWrapperAdapter
import com.intellij.openapi.project.Project
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
        form.items.setListData(items.toTypedArray())
        form.items.setCellRenderer(MFSelectorCellRenderer())
        return form.panel
    }

    override fun getSuccessResult(): List<MFSelectorItem> = items
}
package com.elpassion.intelijidea.action.configure.selector.ui

import com.elpassion.intelijidea.action.configure.selector.MFSelectorItem
import com.intellij.ui.CheckBoxList
import javax.swing.ListSelectionModel

class MFSelectorList : CheckBoxList<MFSelectorItem>() {

    init {
        selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
    }

    fun selectAll() = Unit

    fun unselectAll() = Unit
}
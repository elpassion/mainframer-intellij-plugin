package com.elpassion.intelijidea.action.configure.selector.ui

import com.elpassion.intelijidea.action.configure.selector.MFSelectorItem
import com.intellij.ui.CheckBoxList
import com.jgoodies.common.collect.ArrayListModel
import javax.swing.JCheckBox
import javax.swing.ListSelectionModel

class MFSelectorList : CheckBoxList<MFSelectorItem>() {

    init {
        selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
    }

    var items: List<MFSelectorItem> = emptyList()
        set(value) {
            model = value.asListModel()
            field = value
        }
        get() = field.mapIndexed { index, mfSelectorItem -> mfSelectorItem.copy(isSelected = isItemSelected(index)) }

    @Suppress("unchecked_cast")
    private val listModel = model as ArrayListModel<JCheckBox>

    fun selectAll() {
        listModel.forEachIndexed { index, checkBox ->
            checkBox.isSelected = true
            listModel.fireContentsChanged(index)
        }
    }

    fun unselectAll() {
        listModel.forEachIndexed { index, checkBox ->
            checkBox.isSelected = false
            listModel.fireContentsChanged(index)
        }
    }

    private fun List<MFSelectorItem>.asListModel() = ArrayListModel(map { createCheckBox(it) })

    private fun createCheckBox(item: MFSelectorItem) = JCheckBox(item.getName()).apply { isSelected = item.isSelected }
}
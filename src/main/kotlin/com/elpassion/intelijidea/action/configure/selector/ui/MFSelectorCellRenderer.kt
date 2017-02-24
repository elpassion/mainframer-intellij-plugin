package com.elpassion.intelijidea.action.configure.selector.ui

import com.elpassion.intelijidea.action.configure.selector.MFSelectorItem
import com.intellij.ui.components.JBCheckBox
import java.awt.Component
import javax.swing.JList
import javax.swing.ListCellRenderer

class MFSelectorCellRenderer : JBCheckBox(), ListCellRenderer<MFSelectorItem> {

    override fun getListCellRendererComponent(list: JList<out MFSelectorItem>, value: MFSelectorItem?, index: Int,
                                              isSelected: Boolean, cellHasFocus: Boolean): Component {
        componentOrientation = list.componentOrientation
        font = list.font
        background = list.background
        foreground = list.foreground
        setSelected(value?.isSelected ?: isSelected)
        isEnabled = list.isEnabled
        value?.configuration?.icon?.let { setTextIcon(it) }
        text = value?.configuration?.name?.toString() ?: ""

        return this
    }
}
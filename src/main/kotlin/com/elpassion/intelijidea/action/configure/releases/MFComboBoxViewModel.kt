package com.elpassion.intelijidea.action.configure.releases

import com.elpassion.intelijidea.util.mfVersion
import javax.swing.ComboBoxModel
import javax.swing.event.ListDataListener

class MFComboBoxViewModel(private val versions: List<String>) : ComboBoxModel<String> {
    private var selectedVersion: String? = versions.firstOrNull()

    override fun getSize() = mfVersion.length

    override fun removeListDataListener(l: ListDataListener?) {
    }

    override fun getElementAt(index: Int): String {
        return versions[index]
    }

    override fun setSelectedItem(anItem: Any) {
        selectedVersion = anItem as String
    }

    override fun getSelectedItem() = selectedVersion

    override fun addListDataListener(l: ListDataListener?) {
    }

}
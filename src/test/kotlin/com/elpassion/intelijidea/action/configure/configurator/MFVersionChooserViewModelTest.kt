package com.elpassion.intelijidea.action.configure.configurator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MFVersionChooserViewModelTest {

    @Test
    fun shouldDefaultSelectionNullWhenEmptyList() {
        val mfComboBoxViewModel = MFVersionChooserViewModel(emptyList())

        assertNull(mfComboBoxViewModel.selectedItem)
    }

    @Test
    fun shouldDefaultSelectionFirstItem() {
        val mfComboBoxViewModel = MFVersionChooserViewModel(listOf("Item 1", "Item 2"))

        assertEquals("Item 1", mfComboBoxViewModel.selectedItem)
    }

    @Test
    fun shouldRememberSelectedItem() {
        val mfComboBoxViewModel = MFVersionChooserViewModel(emptyList())

        assertNull(mfComboBoxViewModel.selectedItem)
    }

    @Test
    fun shouldReallyRememberSelectedItem() {
        val mfComboBoxViewModel = MFVersionChooserViewModel(listOf())

        mfComboBoxViewModel.selectedItem = "My item 2"

        assertEquals("My item 2", mfComboBoxViewModel.selectedItem)
    }
}

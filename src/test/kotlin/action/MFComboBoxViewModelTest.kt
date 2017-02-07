package action

import com.elpassion.intelijidea.action.configure.releases.MFComboBoxViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MFComboBoxViewModelTest {

    @Test
    fun shouldDefaultSelectionNullWhenEmptyList() {
        val mfComboBoxViewModel = MFComboBoxViewModel(emptyList())

        assertNull(mfComboBoxViewModel.selectedItem)
    }

    @Test
    fun shouldDefaultSelectionFirstItem() {
        val mfComboBoxViewModel = MFComboBoxViewModel(listOf("Item 1", "Item 2"))

        assertEquals("Item 1", mfComboBoxViewModel.selectedItem)
    }

    @Test
    fun shouldRememberSelectedItem() {
        val mfComboBoxViewModel = MFComboBoxViewModel(emptyList())

        assertNull(mfComboBoxViewModel.selectedItem)
    }

    @Test
    fun shouldReallyRememberSelectedItem() {
        val mfComboBoxViewModel = MFComboBoxViewModel(listOf())

        mfComboBoxViewModel.setSelectedItem("My item 2")

        assertEquals("My item 2", mfComboBoxViewModel.selectedItem)
    }
}

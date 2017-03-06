package com.elpassion.intelijidea.action.configure.selector

import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test

class MFSelectorResultTest {

    @Test
    fun shouldReturnEmptyResultWhenNoConfigurationInProject() {
        val (toInject, toRestore) = getSelectorResult(emptyList(), emptyList())
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnEmptyResultWhenNoChangesInUi() {
        val items = listOf(createSelectorItem())
        val (toInject, toRestore) = getSelectorResult(items, items)
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnConfigurationToInjectWhenSelectedInUi() {
        val item = createSelectorItem()
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = true)))
        assertEquals(listOf(item.configuration), toInject)
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnConfigurationToRestoreWhenUnselectedInUi() {
        val item = createSelectorItem(isSelected = true)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = false)))
        assertTrue(toInject.isEmpty())
        assertEquals(listOf(item.configuration), toRestore)
    }

    @Test
    fun shouldReturnOnlyChangedConfigurationsOnChangesInUi() {
        val item1 = createSelectorItem()
        val item2 = createSelectorItem(isSelected = true)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item1, item2),
                uiOut = listOf(item1, item2.copy(isSelected = false)))
        assertTrue(toInject.isEmpty())
        assertEquals(listOf(item2.configuration), toRestore)
    }

    private fun createSelectorItem(isSelected: Boolean = false) =
            MFSelectorItem(mock(), isTemplate = false, isSelected = isSelected)
}
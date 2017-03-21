package com.elpassion.mainframerplugin.action.configure.selector

import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Test

class MFSelectorResultTest {

    @Test
    fun shouldReturnEmptyResultWhenNoConfigurationInProject() {
        val (toInject, toRestore) = getSelectorResult(emptyList(), emptyList(), replaceAll = false)
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnEmptyResultWhenNoChangesInUi() {
        val items = listOf(createSelectorItem())
        val (toInject, toRestore) = getSelectorResult(items, items, replaceAll = false)
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnConfigurationToInjectWhenSelectedInUi() {
        val item = createSelectorItem()
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = true)),
                replaceAll = false)
        assertEquals(listOf(item.configuration), toInject)
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnConfigurationToRestoreWhenUnselectedInUi() {
        val item = createSelectorItem(isSelected = true)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = false)),
                replaceAll = false)
        assertTrue(toInject.isEmpty())
        assertEquals(listOf(item.configuration), toRestore)
    }

    @Test
    fun shouldReturnOnlyChangedConfigurationsOnChangesInUi() {
        val item1 = createSelectorItem()
        val item2 = createSelectorItem(isSelected = true)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item1, item2),
                uiOut = listOf(item1, item2.copy(isSelected = false)),
                replaceAll = false)
        assertTrue(toInject.isEmpty())
        assertEquals(listOf(item2.configuration), toRestore)
    }

    @Test
    fun shouldNotReplaceAllMfConfigurationsWhenNotSelectedInUi() {
        val items = listOf(createSelectorItem(isSelected = true))
        val result = getSelectorResult(items, items, replaceAll = false)
        assertTrue(result.toInject.isEmpty())
    }

    @Test
    fun shouldReplaceAllMfConfigurationsWhenSelectedInUi() {
        val selectorItem = createSelectorItem(isSelected = true)
        val items = listOf(selectorItem)
        val (toInject, _) = getSelectorResult(
                uiIn = items,
                uiOut = items,
                replaceAll = true)

        assertEquals(listOf(selectorItem.configuration), toInject)
    }

    private fun createSelectorItem(isSelected: Boolean = false) =
            MFSelectorItem(mock(), isSelected = isSelected, name = "configuration")
}
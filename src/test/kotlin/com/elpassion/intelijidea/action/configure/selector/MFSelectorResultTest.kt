package com.elpassion.intelijidea.action.configure.selector

import com.intellij.execution.configurations.RunConfiguration
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MFSelectorResultTest {

    @Test
    fun shouldReturnEmptyResultWhenNoConfigurationInProject() {
        val (toInject, toRestore) = getSelectorResult(emptyList(), emptyList())
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnEmptyResultWhenNoChangesInUi() {
        val items = listOf(MFSelectorItem(mock(), false, false))
        val (toInject, toRestore) = getSelectorResult(items, items)
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnConfigurationToInjectWhenSelectedInUi() {
        val configuration = mock<RunConfiguration>()
        val item = MFSelectorItem(configuration, false, false)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = true)))
        assertEquals(listOf(configuration), toInject)
        assertTrue(toRestore.isEmpty())
    }

    @Test
    fun shouldReturnConfigurationToRestoreWhenUnselectedInUi() {
        val configuration = mock<RunConfiguration>()
        val item = MFSelectorItem(configuration, false, true)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = false)))
        assertTrue(toInject.isEmpty())
        assertEquals(listOf(configuration), toRestore)
    }
}
package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.android.commons.rxjavatest.thenJust
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever

class MFSelectorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val uiSelector = mock<MFUiSelector>()

    fun testShouldReturnEmptyItemsListWhenNoConfigurationInProject() {
        val result = MFSelectorResult(emptyList(), emptyList())
        whenever(uiSelector.invoke(any())).thenJust(result)
        mfSelector(project, uiSelector).test().assertValue { it == result }
    }

    fun testShouldReturnSelectedConfigurationOnChangeInUi() {
        val configuration = mock<RunConfiguration>()
        val result = MFSelectorResult(listOf(configuration), emptyList())
        whenever(uiSelector.invoke(any())).thenJust(result)
        mfSelector(project, uiSelector).test().assertValue { it == result }
    }

    fun testShouldReturnEmptyResultWhenNoConfigurationInProject() {
        val (toInject, toRestore) = getSelectorResult(emptyList(), emptyList())
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    fun testShouldReturnEmptyResultWhenNoChangesInUi() {
        val items = listOf(MFSelectorItem(mock(), false, false))
        val (toInject, toRestore) = getSelectorResult(items, items)
        assertTrue(toInject.isEmpty())
        assertTrue(toRestore.isEmpty())
    }

    fun testShouldReturnSelectedItemOnChangeInUi() {
        val configuration = mock<RunConfiguration>()
        val item = MFSelectorItem(configuration, false, false)
        val (toInject, toRestore) = getSelectorResult(
                uiIn = listOf(item),
                uiOut = listOf(item.copy(isSelected = true)))
        assertEquals(listOf(configuration), toInject)
        assertTrue(toRestore.isEmpty())
    }
}
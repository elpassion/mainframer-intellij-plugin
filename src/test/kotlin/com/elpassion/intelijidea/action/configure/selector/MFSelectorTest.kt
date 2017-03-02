package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.android.commons.rxjavatest.thenJust
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever

class MFSelectorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val uiSelector = mock<MFUiSelector>()

    fun testShouldReturnEmptyItemsListWhenNoConfigurationInProject() {
        whenever(uiSelector.invoke(any())).thenJust(emptyList())
        mfSelector(project, uiSelector).test().assertValue { it.isEmpty() }
    }

    fun testShouldReturnSelectedConfigurationOnChangeInUi() {
        val selectorItem = MFSelectorItem(mock(), false, false)
        whenever(uiSelector.invoke(any())).thenJust(listOf(selectorItem.copy(isSelected = true)))
        mfSelector(project, uiSelector).test().assertValue { it == listOf(selectorItem.copy(isSelected = true)) }
    }

    fun testShouldReturnEmptyResultWhenNoConfigurationInProject() {
        val result = getSelectorResult(emptyList(), emptyList())
        assertTrue(result.isEmpty())
    }

    fun testShouldReturnEmptyResultWhenNoChangesInUi() {
        val items = listOf(MFSelectorItem(mock(), false, false))
        val result = getSelectorResult(items, items)
        assertTrue(result.isEmpty())
    }
}
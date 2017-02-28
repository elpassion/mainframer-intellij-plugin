package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.android.commons.rxjavatest.thenJust
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe

class MFSelectorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val selectorFromUi = mock<(List<MFSelectorItem>) -> Maybe<List<MFSelectorItem>>>()

    fun testShouldReturnEmptyItemsListWhenNoConfigurationInProject() {
        whenever(selectorFromUi.invoke(any())).thenJust(emptyList())
        mfSelector(project, selectorFromUi).test().assertValue { it.isEmpty() }
    }

    fun testShouldReturnNotSelectedConfigurationWhenNoChangeInSelector() {
        val selectorItem = MFSelectorItem(mock(), false, false)
        whenever(selectorFromUi.invoke(any())).thenJust(listOf(selectorItem))
        mfSelector(project, selectorFromUi).test().assertValue { it == listOf(selectorItem) }
    }
}
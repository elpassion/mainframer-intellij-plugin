package com.elpassion.intelijidea.action.configure.selector

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single

class MFSelectorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val selectorFromUi = mock<(List<MFSelectorItem>) -> Single<List<MFSelectorItem>>>()

    fun testShouldReturnEmptyItemsListWhenNoConfigurationInProject() {
        whenever(selectorFromUi.invoke(any())).thenReturn(Single.just(emptyList()))
        mfSelector(project, selectorFromUi).test().assertValue { it.isEmpty() }
    }

    fun testShouldReturnNotSelectedConfigurationWhenNoChangeInSelector() {
        val selectorItem = MFSelectorItem(mock(), false, false)
        whenever(selectorFromUi.invoke(any())).thenReturn(Single.just(listOf(selectorItem)))
        mfSelector(project, selectorFromUi).test().assertValue { it == listOf(selectorItem) }
    }
}
package com.elpassion.intelijidea.action.configure.selector

import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable

class MFSelectorTest : LightPlatformCodeInsightFixtureTestCase() {

    private val selectorFromUi = mock<(Project) -> Observable<MFSelectorItem>>()

    fun testShouldReturnEmptyItemsListWhenNoConfigurationInProject() {
        whenever(selectorFromUi.invoke(any())).thenReturn(Observable.empty())
        mfSelector(project, selectorFromUi).test().assertValue { it.isEmpty() }
    }

    fun testShouldReturnNotSelectedConfigurationWhenNoChangeInSelector() {
        val selectorItem = MFSelectorItem(mock(), false)
        whenever(selectorFromUi.invoke(any())).thenReturn(Observable.just(selectorItem))
        mfSelector(project, selectorFromUi).test().assertValue { it == listOf(selectorItem) }
    }
}
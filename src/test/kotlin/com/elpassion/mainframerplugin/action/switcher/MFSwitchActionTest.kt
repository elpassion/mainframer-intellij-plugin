package com.elpassion.mainframerplugin.action.switcher

import com.elpassion.mainframerplugin.common.MFStateProvider
import com.elpassion.mainframerplugin.util.MFIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat

class MFSwitchActionTest : LightPlatformCodeInsightFixtureTestCase() {

    private val event by lazy {
        mock<AnActionEvent>().also {
            whenever(it.project).thenReturn(project)
            whenever(it.presentation).thenReturn(Presentation())
        }
    }

    fun testShouldSetEnableMainframerIconIfMainframerIsDisabledOnUpdate() {
        MFStateProvider.getInstance(project).apply { isTurnOn = false }
        MFSwitchAction().update(event)
        assertThat(event.presentation.icon).isEqualTo(MFIcons.enableMainframerIcon)
    }

    fun testShouldSetDisableMainframerIconIfMainframerIsEnabledOnUpdate() {
        MFStateProvider.getInstance(project).apply { isTurnOn = true }
        MFSwitchAction().update(event)
        assertThat(event.presentation.icon).isEqualTo(MFIcons.disableMainframerIcon)
    }
}
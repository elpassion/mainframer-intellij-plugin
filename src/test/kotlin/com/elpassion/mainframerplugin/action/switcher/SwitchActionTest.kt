package com.elpassion.mainframerplugin.action.switcher

import com.elpassion.mainframerplugin.common.StateProvider
import com.elpassion.mainframerplugin.util.MainframerIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat

class SwitchActionTest : LightPlatformCodeInsightFixtureTestCase() {

    private val event by lazy {
        mock<AnActionEvent>().also {
            whenever(it.project).thenReturn(project)
            whenever(it.presentation).thenReturn(Presentation())
        }
    }

    fun testShouldSetEnableMainframerIconIfMainframerIsDisabledOnUpdate() {
        StateProvider.getInstance(project).apply { isTurnOn = false }
        SwitchAction().update(event)
        assertThat(event.presentation.icon).isEqualTo(MainframerIcons.enableMainframerIcon)
    }

    fun testShouldSetDisableMainframerIconIfMainframerIsEnabledOnUpdate() {
        StateProvider.getInstance(project).apply { isTurnOn = true }
        SwitchAction().update(event)
        assertThat(event.presentation.icon).isEqualTo(MainframerIcons.disableMainframerIcon)
    }
}
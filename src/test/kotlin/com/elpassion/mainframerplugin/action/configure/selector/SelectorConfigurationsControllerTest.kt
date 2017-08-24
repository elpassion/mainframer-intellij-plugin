package com.elpassion.mainframerplugin.action.configure.selector

import com.elpassion.mainframerplugin.action.select.SelectorConfigurationsController
import com.intellij.execution.configurations.RunConfiguration
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import org.junit.Test

class SelectorConfigurationsControllerTest {

    private val selectorResults = mock<() -> Maybe<SelectorResult>>()
    private val taskSettingsIsValid = mock<() -> Boolean>()
    private val manipulateTasks = mock<(SelectorResult) -> Unit>()
    private val showMessage = mock<(Int, Int) -> Unit>()
    private val showMainframerNotConfiguredError = mock<(String) -> Unit>()
    private val showMainframerTaskInvalidError = mock<(String) -> Unit>()
    private val doesMainframerExists = mock<() -> Boolean>()
    private val controller = SelectorConfigurationsController(manipulateTasks, selectorResults, taskSettingsIsValid, showMessage, showMainframerNotConfiguredError, showMainframerTaskInvalidError, doesMainframerExists)

    @Test
    fun shouldNeverCallManipulateTasksWhenSelectorResultEmpty() {
        whenever(selectorResults.invoke()).thenReturn(Maybe.empty())

        controller.configure()

        verify(manipulateTasks, never()).invoke(any())
    }

    @Test
    fun shouldCallManipulateTasksWhenSelectorResultHasValue() {
        stubSelectorResults()

        controller.configure()

        verify(manipulateTasks).invoke(any())
    }

    @Test
    fun shouldCallShowMessageWhenSelectorReturnEmptyInjectAndRestoreList() {
        stubSelectorResults()

        controller.configure()

        verify(showMessage).invoke(0, 0)
    }

    @Test
    fun shouldShowErrorWhenTaskInjectionFails() {
        stubSelectorResults(toInject = createSingletonListMockConfiguration())
        whenever(taskSettingsIsValid.invoke()).thenReturn(false)
        whenever(doesMainframerExists.invoke()).thenReturn(true)

        controller.configure()

        verify(showMainframerTaskInvalidError).invoke(any())
    }

    @Test
    fun shouldCallShowMessageWhenTaskInjectionSuccessful() {
        val toInject = createSingletonListMockConfiguration()
        stubSelectorResults(toInject = toInject)
        whenever(taskSettingsIsValid.invoke()).thenReturn(true)
        whenever(doesMainframerExists.invoke()).thenReturn(true)

        controller.configure()

        verify(showMessage).invoke(eq(toInject.size), any())
    }

    @Test
    fun shouldCallShowSuccessMessageWhenTaskSettingsInvalidAndInjectionListEmpty() {
        val toRestore = createSingletonListMockConfiguration()
        stubSelectorResults(toRestore = toRestore)
        whenever(taskSettingsIsValid.invoke()).thenReturn(false)
        whenever(doesMainframerExists.invoke()).thenReturn(true)

        controller.configure()

        verify(showMessage).invoke(any(), eq(toRestore.size))
    }

    @Test
    fun shouldShowMainframerNotConfiguredErrorWhenMainframerDoesNotExists() {
        val toInject = createSingletonListMockConfiguration()
        stubSelectorResults(toInject = toInject)
        whenever(taskSettingsIsValid.invoke()).thenReturn(true)
        whenever(doesMainframerExists.invoke()).thenReturn(false)

        controller.configure()

        verify(showMainframerNotConfiguredError).invoke(any())
    }

    private fun createSingletonListMockConfiguration() = listOf(mock<RunConfiguration>())

    private fun stubSelectorResults(toInject: List<RunConfiguration> = listOf(), toRestore: List<RunConfiguration> = listOf()) {
        whenever(selectorResults.invoke()).thenReturn(Maybe.just(SelectorResult(toInject, toRestore)))
    }

}

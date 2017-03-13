package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.action.select.MFSelectorConfigurationsController
import com.intellij.execution.configurations.RunConfiguration
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import org.junit.Test

class MFSelectorConfigurationsControllerTest {

    private val selectorResults = mock<() -> Maybe<MFSelectorResult>>()
    private val taskSettingsIsValid = mock<() -> Boolean>()
    private val manipulateTasks = mock<(MFSelectorResult) -> Unit>()
    private val showMessage = mock<(Int, Int) -> Unit>()
    private val showError = mock<(String) -> Unit>()
    private val controller = MFSelectorConfigurationsController(manipulateTasks, selectorResults, taskSettingsIsValid, showMessage, showError)

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

        controller.configure()

        verify(showError).invoke(any())
    }

    @Test
    fun shouldCallShowMessageWhenTaskInjectionSuccessful() {
        val toInject = createSingletonListMockConfiguration()
        stubSelectorResults(toInject = toInject)
        whenever(taskSettingsIsValid.invoke()).thenReturn(true)

        controller.configure()

        verify(showMessage).invoke(eq(toInject.size), any())
    }

    @Test
    fun shouldCallShowSuccessMessageWhenTaskSettingsInvalidAndInjectionListEmpty() {
        val toRestore = createSingletonListMockConfiguration()
        stubSelectorResults(toRestore = toRestore)
        whenever(taskSettingsIsValid.invoke()).thenReturn(false)

        controller.configure()

        verify(showMessage).invoke(any(), eq(toRestore.size))
    }

    private fun createSingletonListMockConfiguration() = listOf(mock<RunConfiguration>())

    private fun stubSelectorResults(toInject: List<RunConfiguration> = listOf(), toRestore: List<RunConfiguration> = listOf()) {
        whenever(selectorResults.invoke()).thenReturn(Maybe.just(MFSelectorResult(toInject, toRestore)))
    }

}

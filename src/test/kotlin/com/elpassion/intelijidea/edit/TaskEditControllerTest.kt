package com.elpassion.intelijidea.edit

import com.elpassion.intelijidea.task.edit.TaskEditController
import com.elpassion.intelijidea.task.edit.TaskEditView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class TaskEditControllerTest {

    val view = mock<TaskEditView>()
    val actions = mock<TaskEditView.Actions>()
    val controller = TaskEditController(view, actions)

    val taskNameSubject: PublishSubject<String> = PublishSubject.create<String>()

    @Before
    fun setUp() {
        stubTaskNameAction()
    }

    @Test
    fun shouldListenOnTaskNameChangesOnCreate() {
        controller.onCreate()

        verify(actions).observeTaskName()
    }

    @Test
    fun shouldEnableAcceptButtonWhenTaskNameIsNotEmpty() {
        controller.onCreate()
        taskNameSubject.onNext("Not empty text")

        verify(view).enableAcceptButton()
    }


    @Test
    fun shouldDisableAcceptButtonWhenTaskNameIsEmpty() {
        controller.onCreate()
        taskNameSubject.onNext("")

        verify(view).disableAcceptButton()
    }

    @Test
    fun shouldDisableAcceptButtonWhenTaskNameIsBlank() {
        controller.onCreate()
        taskNameSubject.onNext("          ")

        verify(view).disableAcceptButton()
    }

    private fun stubTaskNameAction() {
        whenever(actions.observeTaskName()).thenReturn(taskNameSubject)
    }

}
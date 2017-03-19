package com.elpassion.intelijidea.task

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import io.reactivex.internal.schedulers.ExecutorScheduler

val UINonModalScheduler = ExecutorScheduler { command ->
    ApplicationManager.getApplication().invokeLater(command, ModalityState.NON_MODAL)
}
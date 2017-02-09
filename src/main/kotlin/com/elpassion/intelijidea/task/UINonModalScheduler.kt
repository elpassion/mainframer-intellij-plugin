package com.elpassion.intelijidea.task

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler
import java.util.concurrent.Executor

class UINonModalScheduler : Scheduler() {

    override fun createWorker(): Worker {
        return ExecutorScheduler.ExecutorWorker(UIExecutor)
    }

    private object UIExecutor : Executor {
        override fun execute(command: Runnable) {
            ApplicationManager.getApplication().invokeLater(command, ModalityState.NON_MODAL)
        }
    }
}
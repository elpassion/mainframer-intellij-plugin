package com.elpassion.intelijidea.common

import com.intellij.openapi.application.ApplicationManager
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler
import java.util.concurrent.Executor

object UIScheduler : Scheduler() {

    override fun createWorker(): Worker {
        return ExecutorScheduler.ExecutorWorker(UIExecutor)
    }

    private object UIExecutor : Executor {
        override fun execute(command: Runnable) {
            ApplicationManager.getApplication().invokeLater(command)
        }
    }
}
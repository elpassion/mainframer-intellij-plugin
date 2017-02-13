package com.elpassion.intelijidea.common

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler

class ProgressScheduler(private val project: Project?, private val description: String) : Scheduler() {
    override fun createWorker(): Worker {
        return ExecutorScheduler.ExecutorWorker { command ->
            ProgressManager.getInstance().run(object : Task.Backgroundable(project, description) {
                override fun run(indicator: ProgressIndicator) {
                    command.run()
                }
            })
        }
    }
}
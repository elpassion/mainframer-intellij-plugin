package com.elpassion.mainframerplugin.common

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import io.reactivex.internal.schedulers.ExecutorScheduler

fun ProgressScheduler(project: Project?, description: String) = ExecutorScheduler { command ->
    ProgressManager.getInstance().run(object : Task.Backgroundable(project, description) {
        override fun run(indicator: ProgressIndicator) {
            command.run()
        }
    })
}
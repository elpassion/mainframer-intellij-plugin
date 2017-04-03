package com.elpassion.mainframerplugin.common

import com.intellij.openapi.application.ApplicationManager
import io.reactivex.internal.schedulers.ExecutorScheduler

val UIScheduler = ExecutorScheduler { command ->
    ApplicationManager.getApplication().invokeLater(command)
}
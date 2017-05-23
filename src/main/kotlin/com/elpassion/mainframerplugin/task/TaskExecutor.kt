package com.elpassion.mainframerplugin.task

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.functions.Consumer

class TaskExecutor(private val project: Project,
                   private val commandLineProvider: (Project, TaskData) -> GeneralCommandLine) {

    fun executeSync(task: MainframerTask, executionId: Long): Boolean {
        return Single.fromCallable { createExecutionEnv(task, executionId) }
                .subscribeOn(UINonModalScheduler)
                .doAfterSuccess { saveAllDocuments() }
                .flatMap(executeAsync)
                .observeOn(UINonModalScheduler)
                .map { it.exitCode == 0 }
                .doAfterSuccess(syncFiles)
                .onErrorReturnItem(false)
                .blockingGet()
    }

    private fun createExecutionEnv(task: MainframerTask, executionId: Long): ExecutionEnvironment {
        return ExecutionEnvironmentBuilder(project, DefaultRunExecutor.getRunExecutorInstance())
                .runProfile(MainframerRunProfile(task, { taskData: TaskData -> commandLineProvider(project, taskData) }))
                .build()
                .apply {
                    this.executionId = executionId
                }
    }

    private val executeAsync: (ExecutionEnvironment) -> Single<ProcessEvent> = { env ->
        Single.create { emitter ->
            env.runner.execute(env) {
                it.processHandler?.addProcessListener(EmitOnTerminatedProcessAdapter(emitter))
            }
        }
    }

    private class EmitOnTerminatedProcessAdapter(private val emitter: SingleEmitter<ProcessEvent>) : ProcessAdapter() {

        override fun processTerminated(event: ProcessEvent) {
            emitter.onSuccess(event)
        }
    }

    private val syncFiles = Consumer<Boolean> {
        ApplicationManager.getApplication().runWriteAction {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }
}

private fun saveAllDocuments() = FileDocumentManager.getInstance().saveAllDocuments()
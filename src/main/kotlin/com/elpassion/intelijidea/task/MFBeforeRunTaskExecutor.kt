package com.elpassion.intelijidea.task

import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.execution.util.ExecutionErrorDialog
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.functions.Consumer
import org.jetbrains.rpc.LOG

class MFBeforeRunTaskExecutor(private val project: Project) {

    fun executeSync(task: MFBeforeRunTask, executionId: Long): Boolean {
        return Single.fromCallable { createExecutionEnv(task, executionId) }
                .subscribeOn(UINonModalScheduler)
                .doAfterSuccess { saveAllDocuments() }
                .flatMap(executeAsync)
                .observeOn(UINonModalScheduler)
                .map { it.exitCode == 0 }
                .doAfterSuccess(syncFiles)
                .doOnError(logError)
                .onErrorReturnItem(false)
                .blockingGet()
    }

    private fun createExecutionEnv(task: MFBeforeRunTask, executionId: Long): ExecutionEnvironment {
        return ExecutionEnvironmentBuilder(project, DefaultRunExecutor.getRunExecutorInstance())
                .runProfile(MFBeforeRunTaskProfile(task))
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

    private val logError: (Throwable) -> Unit = {
        LOG.error(it)
        if (it is ExecutionException) {
            ExecutionErrorDialog.show(it, "Mainframer error!", project)
        }
    }
}

private fun saveAllDocuments() = FileDocumentManager.getInstance().saveAllDocuments()
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
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.jetbrains.rpc.LOG

class MFBeforeRunTaskExecutor(private val project: Project) {

    fun executeSync(task: MFBeforeRunTask): Boolean {
        return Observable.fromCallable { createExecutionEnv(task) }
                .subscribeOn(UINonModalScheduler)
                .observeOn(UINonModalScheduler)
                .doOnNext(saveFiles)
                .flatMap(executeAsync)
                .observeOn(UINonModalScheduler)
                .map { it.exitCode == 0 }
                .doOnNext(syncFiles)
                .doOnError(logError)
                .onErrorReturnItem(false)
                .blockingSingle()
    }

    private fun createExecutionEnv(task: MFBeforeRunTask): ExecutionEnvironment {
        return ExecutionEnvironmentBuilder(project, DefaultRunExecutor.getRunExecutorInstance())
                .runProfile(MFBeforeRunTaskProfile(task))
                .build()
                .apply {
                    executionId = 0L
                }
    }

    private val saveFiles: (ExecutionEnvironment) -> Unit = {
        FileDocumentManager.getInstance().saveAllDocuments()
    }

    private val executeAsync: (ExecutionEnvironment) -> Observable<ProcessEvent> = { env ->
        val publisher = PublishSubject.create<ProcessEvent>()
        env.runner.execute(env) { descriptor ->
            val processHandler = descriptor.processHandler
            processHandler?.addProcessListener(EmitOnTerminatedProcessAdapter(publisher))
        }
        publisher
    }

    private class EmitOnTerminatedProcessAdapter(private val publisher: PublishSubject<ProcessEvent>) : ProcessAdapter() {

        override fun processTerminated(event: ProcessEvent) {
            publisher.onNext(event)
            publisher.onComplete()
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
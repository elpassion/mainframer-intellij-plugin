package com.elpassion.intelijidea

import com.elpassion.intelijidea.util.showBalloon
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.execution.util.ExecutionErrorDialog
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.Ref
import com.intellij.util.concurrency.Semaphore
import org.jetbrains.rpc.LOG
import javax.swing.SwingUtilities

class MFBeforeRunTaskProvider : BeforeRunTaskProvider<MFBeforeRunTask>() {

    override fun getDescription(task: MFBeforeRunTask): String = TASK_NAME

    override fun getName(): String = TASK_NAME

    override fun isConfigurable(): Boolean = false

    override fun configureTask(runConfiguration: RunConfiguration?, task: MFBeforeRunTask?): Boolean = false

    override fun getId(): Key<MFBeforeRunTask> = ID

    override fun canExecuteTask(configuration: RunConfiguration?, task: MFBeforeRunTask?): Boolean {
        //TODO: change
        return true
    }

    override fun executeTask(context: DataContext, configuration: RunConfiguration?, env: ExecutionEnvironment?, task: MFBeforeRunTask?): Boolean {
        SwingUtilities.invokeAndWait {
            showBalloon(configuration?.project, "Mainframer is executing task: ${configuration?.name}")
        }
        return executeSync(context)
    }

    fun executeSync(context: DataContext): Boolean {
        val targetDone = Semaphore()
        val result = Ref(false)

        try {
            ApplicationManager.getApplication().invokeAndWait({
                executeImpl(context, 0L, object : ProcessAdapter() {
                    override fun startNotified(event: ProcessEvent?) {
                        targetDone.down()
                    }

                    override fun processTerminated(event: ProcessEvent?) {
                        result.set(event!!.exitCode == 0)
                        targetDone.up()
                    }
                })
            }, ModalityState.NON_MODAL)
        } catch (e: Exception) {
            LOG.error(e)
            return false
        }

        targetDone.waitFor()
        return result.get()
    }

    private fun executeImpl(dataContext: DataContext, executionId: Long, processListener: ProcessAdapter) {
        val project = CommonDataKeys.PROJECT.getData(dataContext) ?: return

        FileDocumentManager.getInstance().saveAllDocuments()
        try {
            val environment = ExecutionEnvironmentBuilder.create(project, DefaultRunExecutor.getRunExecutorInstance(), MFBeforeRunTaskProfile()).build()
            environment.executionId = executionId
            environment.runner.execute(environment) { descriptor ->
                val processHandler = descriptor.processHandler
                if (processHandler != null) {
                    LOG.assertTrue(!processHandler.isStartNotified, "ProcessHandler is already startNotified, the listener won't be correctly notified")
                    processHandler.addProcessListener(processListener)
                }
            }
        } catch (ex: ExecutionException) {
            ExecutionErrorDialog.show(ex, "Mainframer error title", project)
        }
    }

    override fun createTask(runConfiguration: RunConfiguration?): MFBeforeRunTask? {
        val task = MFBeforeRunTask()
        task.isEnabled = true
        return task
    }

    companion object {
        val ID = Key.create<MFBeforeRunTask>("MainFrame.BeforeRunTask")
        val TASK_NAME = "MainframerBefore"
    }
}


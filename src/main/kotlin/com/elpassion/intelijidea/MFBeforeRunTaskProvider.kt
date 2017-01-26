package com.elpassion.intelijidea

import com.elpassion.intelijidea.util.showBalloon
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.util.Key
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

    override fun executeTask(context: DataContext?, configuration: RunConfiguration?, env: ExecutionEnvironment?, task: MFBeforeRunTask?): Boolean {
        //TODO: implement
        SwingUtilities.invokeAndWait {
            showBalloon(configuration?.project, "Mainframer is executing task: ${configuration?.name}")
        }
        return true
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

class MFBeforeRunTask : BeforeRunTask<MFBeforeRunTask>(MFBeforeRunTaskProvider.ID) {

}

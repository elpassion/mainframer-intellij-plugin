package com.elpassion.intelijidea

import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ex.ProjectManagerEx
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.WindowManager


class MFProcessAdapter(private val myProject: Project,
                       private val myName: String) : ProcessAdapter() {

    override fun processTerminated(event: ProcessEvent?) {
        val message = myName + event!!.exitCode

        reflectProjectChanges(message)
        updateStatusBarMessage(message)
    }

    private fun reflectProjectChanges(message: String) {
        ApplicationManager.getApplication().runReadAction {
            VirtualFileManager.getInstance().asyncRefresh {
                updateStatusBarMessage(message)
            }
        }
    }

    private fun updateStatusBarMessage(message: String) {
        if (ProjectManagerEx.getInstanceEx().isProjectOpened(myProject)) {
            val statusBar = WindowManager.getInstance().getStatusBar(myProject)
            if (statusBar != null) {
                statusBar.info = message
            }
        }
    }
}

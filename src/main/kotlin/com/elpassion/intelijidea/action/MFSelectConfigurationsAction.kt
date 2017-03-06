package com.elpassion.intelijidea.action

import com.elpassion.intelijidea.TaskManipulator
import com.elpassion.intelijidea.action.configure.selector.mfSelector
import com.elpassion.intelijidea.action.configure.selector.showSelectorDialog
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class MFSelectConfigurationsAction : AnAction(MF_SELECT_CONFIGURATIONS_ACTION) {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { selectConfigurations(it) }
    }

    private fun selectConfigurations(project: Project) {
        mfSelector(project) { items ->
            showSelectorDialog(project, items)
        }.subscribe { (toInject, toRestore, replaceAll) ->
            TaskManipulator(project).run {
                when (replaceAll) {
                    true -> injectMFToConfigurationsWithReplacingMFTask(project.mfBeforeRunTaskProvider, toInject)
                    false -> injectMFToConfigurations(project.mfBeforeRunTaskProvider, toInject)
                }
                restoreConfigurations(toRestore)
            }
        }
    }

    companion object {
        private val MF_SELECT_CONFIGURATIONS_ACTION = "Select configurations to inject mainframer (incubating feature)"
    }
}
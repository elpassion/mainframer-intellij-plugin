package com.elpassion.intelijidea.action.select

import com.elpassion.intelijidea.action.configure.selector.MFSelectorResult
import com.elpassion.intelijidea.action.configure.selector.mfSelector
import com.elpassion.intelijidea.action.configure.selector.showSelectorDialog
import com.elpassion.intelijidea.task.mfBeforeRunTaskProvider
import com.elpassion.intelijidea.util.showInfo
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
        }
                .map { result -> configureSelections(project, result) }
                .subscribe { (injected, restored) ->
                    project.showConfigurationChangesCountInfo(injected, restored)
                }
    }

    private fun configureSelections(project: Project, selectorResult: MFSelectorResult) = with(selectorResult) {
        TaskManipulator(project).run {
            when (replaceAll) {
                true -> injectMFToConfigurationsWithReplacingMFTask(project.mfBeforeRunTaskProvider, toInject)
                false -> injectMFToConfigurations(project.mfBeforeRunTaskProvider, toInject)
            }
            restoreConfigurations(toRestore)
        }
        toInject.size to toRestore.size
    }

    private fun Project.showConfigurationChangesCountInfo(injected: Int, restored: Int) {
        showInfo(this, "Number of injected configurations $injected, restored to default $restored")
    }

    companion object {
        private val MF_SELECT_CONFIGURATIONS_ACTION = "Select configurations to inject mainframer or restore to default"
    }
}
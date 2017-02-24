package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.getConfigurations
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import io.reactivex.Observable
import io.reactivex.Single

fun mfSelector(project: Project, selectorFromUi: (List<MFSelectorItem>) -> Observable<MFSelectorItem>): Single<List<MFSelectorItem>> {
    val runManager = RunManagerEx.getInstanceEx(project)
    val selectorItems = runManager.getConfigurations()
            .map { MFSelectorItem(it, runManager.hasMainframerTask(it)) }
    return selectorFromUi(selectorItems).toList()
}

private fun RunManagerEx.hasMainframerTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).any { it is MFBeforeRunTask }
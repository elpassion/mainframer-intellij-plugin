package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.getTemplateConfigurations
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import io.reactivex.Observable
import io.reactivex.Single

fun showSelectorDialog(project: Project, selectorItems: List<MFSelectorItem>): Observable<MFSelectorItem> =
        Observable.create<MFSelectorItem> { emitter ->
            MFSelectorDialog(project, selectorItems, {
                selectorItems.forEach { emitter.onNext(it) }
                emitter.onComplete()
            }, {
                emitter.onComplete()
            }).show()
        }

fun mfSelector(project: Project, selectorFromUi: (List<MFSelectorItem>) -> Observable<MFSelectorItem>): Single<List<MFSelectorItem>> {
    val runManager = RunManagerEx.getInstanceEx(project)
    val selectorItems = runManager.allConfigurationsList
            .map { MFSelectorItem(it, isTemplate = false, isSelected = runManager.hasMainframerTask(it)) } +
            runManager.getTemplateConfigurations()
                    .map { MFSelectorItem(it, isTemplate = true, isSelected = runManager.hasMainframerTask(it)) }
    return selectorFromUi(selectorItems).toList()
}

private fun RunManagerEx.hasMainframerTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).any { it is MFBeforeRunTask }
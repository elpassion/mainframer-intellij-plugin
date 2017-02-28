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

fun mfSelector(project: Project, selectorFromUi: (List<MFSelectorItem>) -> Observable<MFSelectorItem>): Single<List<MFSelectorItem>> =
        with(RunManagerEx.getInstanceEx(project)) {
            val selectorItems = getConfigurationItems() + getTemplateConfigurationItems()
            selectorFromUi(selectorItems).toList()
        }

private fun RunManagerEx.getConfigurationItems() = allConfigurationsList
        .map { MFSelectorItem(it, isTemplate = false, isSelected = hasMainframerTask(it)) }

private fun RunManagerEx.getTemplateConfigurationItems() = getTemplateConfigurations()
        .map { MFSelectorItem(it, isTemplate = true, isSelected = hasMainframerTask(it)) }

private fun RunManagerEx.hasMainframerTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).any { it is MFBeforeRunTask }
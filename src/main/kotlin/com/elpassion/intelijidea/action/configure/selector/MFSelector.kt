package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.getConfigurations
import com.intellij.execution.RunManagerEx
import com.intellij.openapi.project.Project
import io.reactivex.Observable
import io.reactivex.Single

fun mfSelector(project: Project, selectorFromUi: (List<MFSelectorItem>) -> Observable<MFSelectorItem>): Single<List<MFSelectorItem>> {
    val runManager = RunManagerEx.getInstance(project)
    val selectorItems = runManager.getConfigurations()
            .map { MFSelectorItem(it, false) }
    return selectorFromUi(selectorItems).toList()
}
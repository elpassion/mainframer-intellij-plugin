package com.elpassion.intelijidea.action.configure.selector

import com.intellij.openapi.project.Project
import io.reactivex.Observable
import io.reactivex.Single

fun mfSelector(project: Project, selectorFromUi: (Project) -> Observable<MFSelectorItem>): Single<List<MFSelectorItem>> {
    return selectorFromUi(project).toList()
}
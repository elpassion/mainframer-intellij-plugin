package com.elpassion.intelijidea.action.configure.selector

import com.intellij.openapi.project.Project
import io.reactivex.Observable

fun mfSelector(project: Project, selectorFromUi: (Project) -> Observable<MFSelectorItem>): Observable<MFSelectorItem> {
    return selectorFromUi(project)
}
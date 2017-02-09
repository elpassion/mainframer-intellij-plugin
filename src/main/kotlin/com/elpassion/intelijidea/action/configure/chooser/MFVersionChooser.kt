package com.elpassion.intelijidea.action.configure.chooser

import com.elpassion.intelijidea.common.Result
import com.intellij.openapi.project.Project
import io.reactivex.Observable

class MFVersionChooser(val project: Project) : Function1<List<String>, Observable<String>> {
    override fun invoke(versionsList: List<String>): Observable<String> {
        return MFVersionChooserDialog(project, versionsList).showAsObservable()
    }
}
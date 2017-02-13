package com.elpassion.intelijidea.action.configure.chooser

import com.intellij.openapi.project.Project

fun mfVersionChooser(project: Project) = { versionsList: List<String> ->
    MFVersionChooserDialog(project, versionsList).showAsObservable()
}
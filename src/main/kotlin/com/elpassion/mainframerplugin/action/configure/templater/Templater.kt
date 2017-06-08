package com.elpassion.mainframerplugin.action.configure.templater

import com.intellij.openapi.project.Project
import io.reactivex.Maybe

fun templater(project: Project): Maybe<ProjectType> {
    return templateApplicationDialog(project)
}
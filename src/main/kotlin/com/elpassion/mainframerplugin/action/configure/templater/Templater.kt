package com.elpassion.mainframerplugin.action.configure.templater

import com.intellij.openapi.project.Project
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import java.io.File

fun templateChooser(project: Project): Maybe<ProjectType> = templateApplicationDialog(project)

fun templateSetter(project: Project): (ProjectType) -> Observable<Pair<String, String>> = { projectType ->
    Observable.fromIterable(listOf("ignore", "remoteignore", "localignore"))
            .map { fileName ->
                val sourceFile = createSourcePath(projectType.resourceDir, fileName)
                val targetFile = createTargetPath(project, fileName)
                sourceFile to targetFile
            }
}

private fun createTargetPath(project: Project, fileName: String) = "${project.basePath}/.mainframer/$fileName"

private fun createSourcePath(projectTypeResourceDir: String, fileName: String) = "templates/$projectTypeResourceDir/$fileName"

typealias FileCopier = (source: String, destination: String) -> Completable
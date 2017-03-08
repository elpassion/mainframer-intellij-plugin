package com.elpassion.intelijidea.configuration

import com.intellij.openapi.project.Project

object RemoteToLocalFileTranslator {
    fun translate(project: Project, remoteFilePath: String): String = remoteFilePath.replace("\\D+mainframer/${project.name}".toRegex()) { project.basePath!! }
}
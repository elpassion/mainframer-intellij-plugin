package com.elpassion.mainframerplugin.common.console

import com.intellij.execution.filters.InputFilter
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair

class RemoteToLocalTranslatingInputFilter(private val project: Project) : InputFilter {

    override fun applyFilter(text: String, contentType: ConsoleViewContentType) = mutableListOf(Pair(translateText(text), contentType))

    private fun translateText(text: String): String {
        val regex = RemoteToLocalInputConverter(project.name).LINE_WITH_REMOTE_EXCEPTION
        return regex.replace(text, "${project.basePath}$1:$2")
    }

}
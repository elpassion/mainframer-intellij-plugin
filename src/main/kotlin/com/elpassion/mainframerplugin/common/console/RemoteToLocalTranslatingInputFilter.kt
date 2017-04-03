package com.elpassion.mainframerplugin.common.console

import com.intellij.execution.filters.InputFilter
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair

class RemoteToLocalTranslatingInputFilter(project: Project) : InputFilter {

    private val converter = RemoteToLocalInputConverter(project.name, project.basePath!!)

    override fun applyFilter(text: String, contentType: ConsoleViewContentType)
            = mutableListOf(Pair(converter.convertInput(text), contentType))

}
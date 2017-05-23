package com.elpassion.mainframerplugin.common.console

import com.intellij.execution.filters.ConsoleInputFilterProvider
import com.intellij.openapi.project.Project

class RemoteToLocalTranslatingInputFilterProvider : ConsoleInputFilterProvider {
    override fun getDefaultFilters(project: Project) = arrayOf(RemoteToLocalTranslatingInputFilter(project))
}

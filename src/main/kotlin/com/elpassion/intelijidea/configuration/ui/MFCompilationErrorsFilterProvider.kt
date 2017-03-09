package com.elpassion.intelijidea.configuration.ui

import com.elpassion.intelijidea.configuration.RemoteToLocalTranslatingFilter
import com.intellij.execution.filters.ConsoleFilterProvider
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project

class MFCompilationErrorsFilterProvider : ConsoleFilterProvider {
    override fun getDefaultFilters(project: Project): Array<Filter> {
        return arrayOf(RemoteToLocalTranslatingFilter(project))
    }
}
package com.elpassion.intelijidea.action.configure.selector

import com.elpassion.intelijidea.getTemplateConfigurations
import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import io.reactivex.Maybe

typealias MFUiSelector = (List<MFSelectorItem>) -> Maybe<List<MFSelectorItem>>

fun mfSelector(project: Project, uiSelector: MFUiSelector): Maybe<List<MFSelectorItem>> =
        with(RunManagerEx.getInstanceEx(project)) {
            uiSelector(getConfigurationItems() + getTemplateConfigurationItems())
        }

fun getSelectorResult(uiIn: List<MFSelectorItem>, uiOut: List<MFSelectorItem>): List<MFSelectorItem> = emptyList()

private fun RunManagerEx.getConfigurationItems() = allConfigurationsList
        .map { MFSelectorItem(it, isTemplate = false, isSelected = hasMainframerTask(it)) }

private fun RunManagerEx.getTemplateConfigurationItems() = getTemplateConfigurations()
        .map { MFSelectorItem(it, isTemplate = true, isSelected = hasMainframerTask(it)) }

private fun RunManagerEx.hasMainframerTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).any { it is MFBeforeRunTask }
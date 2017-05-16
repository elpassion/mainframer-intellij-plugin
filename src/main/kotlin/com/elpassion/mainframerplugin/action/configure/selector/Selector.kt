package com.elpassion.mainframerplugin.action.configure.selector

import com.elpassion.mainframerplugin.action.select.getTemplateConfigurations
import com.elpassion.mainframerplugin.task.MainframerTask
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import io.reactivex.Maybe

typealias UiSelector = (List<SelectorItem>, List<SelectorItem>) -> Maybe<SelectorResult>

fun selector(project: Project, uiSelector: UiSelector) = {
    with(RunManagerEx.getInstanceEx(project)) {
        uiSelector(getConfigurationItems(), getTemplateConfigurationItems())
    }
}

fun getSelectorResult(uiIn: List<SelectorItem>, uiOut: List<SelectorItem>, replaceAll: Boolean): SelectorResult {
    val toInject = getItemsToInject(uiIn, uiOut, replaceAll)
    val toRestore = getItemsToRestore(uiIn, uiOut)
    return SelectorResult(toInject, toRestore)
}

private fun RunManagerEx.getConfigurationItems() = allConfigurationsList
        .map { SelectorItem(it, isSelected = hasMainframerTask(it), name = it.configurationName()) }


private fun RunManagerEx.getTemplateConfigurationItems() = getTemplateConfigurations()
        .map { SelectorItem(it, isSelected = hasMainframerTask(it), name = it.templateConfigurationName()) }

private fun RunConfiguration.configurationName(): String = "[${type.displayName}] ${name}"

private fun RunConfiguration.templateConfigurationName(): String = type.displayName

private fun RunManagerEx.hasMainframerTask(configuration: RunConfiguration) =
        getBeforeRunTasks(configuration).any { it is MainframerTask }

private fun getItemsToInject(uiIn: List<SelectorItem>, uiOut: List<SelectorItem>, replaceAll: Boolean) =
        getItemsToInject(if (replaceAll) emptyList() else uiIn, uiOut)

private fun getItemsToInject(uiIn: List<SelectorItem>, uiOut: List<SelectorItem>) =
        (uiOut.filter { it.isSelected } - uiIn.filter { it.isSelected }).map { it.configuration }

private fun getItemsToRestore(uiIn: List<SelectorItem>, uiOut: List<SelectorItem>) =
        (uiOut.filterNot { it.isSelected } - uiIn.filterNot { it.isSelected }).map { it.configuration }
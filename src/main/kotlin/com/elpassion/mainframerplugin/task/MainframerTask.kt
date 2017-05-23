package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.util.fromJson
import com.elpassion.mainframerplugin.util.toJson
import com.intellij.execution.BeforeRunTask
import org.jdom.Element

class MainframerTask(var data: TaskData) : BeforeRunTask<MainframerTask>(MainframerTaskProvider.ID) {

    fun isValid() = data.isValid()

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute(TASK_DATA, data.toJson())
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        data = element.getAttributeValue(TASK_DATA)?.fromJson<TaskData>() ?: data
    }

    companion object {
        private val TASK_DATA = "mainframer_before_data"
    }
}
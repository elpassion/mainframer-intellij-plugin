package com.elpassion.mainframerplugin.task

import com.elpassion.mainframerplugin.util.fromJson
import com.elpassion.mainframerplugin.util.toJson
import com.intellij.execution.BeforeRunTask
import org.jdom.Element

class MFBeforeRunTask(var data: MFTaskData) : BeforeRunTask<MFBeforeRunTask>(MFBeforeRunTaskProvider.ID) {
    fun isValid() = data.isValid()

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.setAttribute(MF_BEFORE_TASK_DATA, data.toJson())
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        data = element.getAttributeValue(MF_BEFORE_TASK_DATA)?.fromJson<MFTaskData>() ?: data
    }

    companion object {
        private val MF_BEFORE_TASK_DATA = "mainframer_before_data"
    }
}
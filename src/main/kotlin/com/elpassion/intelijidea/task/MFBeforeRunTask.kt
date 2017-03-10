package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.util.fromJson
import com.elpassion.intelijidea.util.toJson
import com.intellij.execution.BeforeRunTask
import org.jdom.Element
import java.io.File
import java.io.Serializable

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

data class MFTaskData(val mainframerPath: String? = null,
                      val buildCommand: String? = null,
                      val taskName: String? = null) : Serializable {

    fun isValid(): Boolean = listOf(mainframerPath, buildCommand, taskName).none { it.isNullOrBlank() } && isScriptValid()

    private fun isScriptValid() = File(mainframerPath).let {
        it.exists() && it.isFile && it.canExecute()
    }
}
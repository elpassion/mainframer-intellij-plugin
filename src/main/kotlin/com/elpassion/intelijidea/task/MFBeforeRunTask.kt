package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.execution.BeforeRunTask
import java.io.File
import java.io.Serializable

class MFBeforeRunTask(var data: MFTaskData?) : BeforeRunTask<MFBeforeRunTask>(MFBeforeRunTaskProvider.ID) {

    fun isValid() = data != null && data!!.isValid && File(data!!.mainframerPath, mfFilename).exists()
}

data class MFTaskData(val mainframerPath: String? = null,
                      val buildCommand: String? = null,
                      val taskName: String? = null) : Serializable {

    val isValid: Boolean get() = mainframerPath != null && buildCommand != null && taskName != null
}
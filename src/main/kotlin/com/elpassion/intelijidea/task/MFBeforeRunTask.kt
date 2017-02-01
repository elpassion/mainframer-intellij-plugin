package com.elpassion.intelijidea.task

import com.elpassion.intelijidea.util.mfFilename
import com.intellij.execution.BeforeRunTask
import java.io.File

class MFBeforeRunTask(var mainframerPath: String?,
                      var buildCommand: String?,
                      var taskName: String?) : BeforeRunTask<MFBeforeRunTask>(MFBeforeRunTaskProvider.ID) {

    fun isValid() = mainframerPath != null &&
            buildCommand != null &&
            taskName != null &&
            File(mainframerPath, mfFilename).exists()
}
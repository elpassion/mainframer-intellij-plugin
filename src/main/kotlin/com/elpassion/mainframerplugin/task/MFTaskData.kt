package com.elpassion.mainframerplugin.task

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.File
import java.io.Serializable

data class MFTaskData(val buildCommand: String = "",
                      val mainframerPath: String = "") : Serializable {

    @JsonIgnore
    fun isValid(): Boolean = listOf(mainframerPath, buildCommand).none(String::isBlank) && isScriptValid()

    @JsonIgnore
    fun isScriptValid() = File(mainframerPath).let {
        it.exists() && it.isFile && it.canExecute()
    }
}
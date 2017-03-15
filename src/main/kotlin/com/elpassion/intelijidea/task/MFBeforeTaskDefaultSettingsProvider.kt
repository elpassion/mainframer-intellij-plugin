package com.elpassion.intelijidea.task

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

@State(name = "MFBeforeTaskDefaultSettingsProvider", storages = arrayOf(Storage("mainframer.xml")))
class MFBeforeTaskDefaultSettingsProvider : PersistentStateComponent<MFBeforeTaskDefaultSettingsProvider.State> {

    var taskData: MFTaskData
        get() = MFTaskData(myState.defaultBuildCommand, myState.defaultTaskName, myState.defaultMainframerPath)
        set(value) = with(myState) {
            defaultMainframerPath = value.mainframerPath
            defaultBuildCommand = value.buildCommand
            defaultTaskName = value.taskName
        }

    private val myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState.defaultBuildCommand = state.defaultBuildCommand
        myState.defaultTaskName = state.defaultTaskName
        myState.defaultMainframerPath = state.defaultMainframerPath
    }

    class State {
        var defaultMainframerPath: String = ""
        var defaultBuildCommand: String = ""
        var defaultTaskName: String = ""
    }

    companion object {
        fun getInstance(project: Project): MFBeforeTaskDefaultSettingsProvider = ServiceManager.getService(project, MFBeforeTaskDefaultSettingsProvider::class.java)
    }
}
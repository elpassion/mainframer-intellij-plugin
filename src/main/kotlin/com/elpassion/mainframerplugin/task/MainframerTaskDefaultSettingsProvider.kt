package com.elpassion.mainframerplugin.task

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

@State(name = "MainframerTaskDefaultSettingsProvider", storages = arrayOf(Storage("mainframer.xml")))
class MainframerTaskDefaultSettingsProvider : PersistentStateComponent<MainframerTaskDefaultSettingsProvider.State> {

    var taskData: TaskData
        get() = TaskData(myState.defaultBuildCommand, myState.defaultMainframerPath)
        set(value) = with(myState) {
            defaultMainframerPath = value.mainframerPath
            defaultBuildCommand = value.buildCommand
        }

    private val myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState.defaultBuildCommand = state.defaultBuildCommand
        myState.defaultMainframerPath = state.defaultMainframerPath
    }

    class State {
        var defaultMainframerPath: String = ""
        var defaultBuildCommand: String = ""
    }

    companion object {
        fun getInstance(project: Project): MainframerTaskDefaultSettingsProvider = ServiceManager.getService(project, MainframerTaskDefaultSettingsProvider::class.java)
    }
}
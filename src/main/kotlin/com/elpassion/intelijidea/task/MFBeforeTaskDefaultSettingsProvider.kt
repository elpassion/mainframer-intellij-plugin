package com.elpassion.intelijidea.task

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "MFBeforeTaskDefaultSettingsProvider", storages = arrayOf(Storage("mainframer.xml")))
class MFBeforeTaskDefaultSettingsProvider : PersistentStateComponent<MFBeforeTaskDefaultSettingsProvider.State> {

    var taskData: MFTaskData
        get() = MFTaskData(myState.defaultMainframerPath, myState.defaultBuildCommand, myState.defaultTaskName)
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
        myState.configureBeforeTaskOnStartup = state.configureBeforeTaskOnStartup
        myState.remoteMachineName = state.remoteMachineName
    }

    class State {
        var defaultMainframerPath: String? = null
        var defaultBuildCommand: String? = null
        var defaultTaskName: String? = null
        var configureBeforeTaskOnStartup: Boolean = false
        var remoteMachineName: String? = null
    }

    companion object {
        val INSTANCE: MFBeforeTaskDefaultSettingsProvider
            get() = ServiceManager.getService(MFBeforeTaskDefaultSettingsProvider::class.java)
    }
}
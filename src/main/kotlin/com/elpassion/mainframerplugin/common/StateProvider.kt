package com.elpassion.mainframerplugin.common

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

@State(name = "StateProvider", storages = arrayOf(Storage("mainframer_state.xml")))
class StateProvider : PersistentStateComponent<StateProvider.State> {

    var isTurnOn: Boolean
        get() = myState.isTurnOn
        set(value) = with(myState) {
            isTurnOn = value
        }

    override fun loadState(state: State) {
        isTurnOn = state.isTurnOn
    }

    override fun getState(): State = myState

    private val myState = State()

    class State {
        var isTurnOn = true
    }

    companion object {
        fun getInstance(project: Project): StateProvider = ServiceManager.getService(project, StateProvider::class.java)
    }
}
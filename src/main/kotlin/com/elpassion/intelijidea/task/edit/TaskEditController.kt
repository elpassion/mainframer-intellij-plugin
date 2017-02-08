package com.elpassion.intelijidea.task.edit


class TaskEditController(private val view: TaskEditView, private val actions: TaskEditView.Actions) {

    fun onCreate() {
        actions.observeTaskName()
                .subscribe {
                    if (it.isBlank()) {
                        view.disableAcceptButton()
                    } else {
                        view.enableAcceptButton()
                    }
                }
    }
}
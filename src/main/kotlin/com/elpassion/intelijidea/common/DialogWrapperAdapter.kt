package com.elpassion.intelijidea.common

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.reactivex.functions.Cancellable

abstract class DialogWrapperAdapter<T>(project: Project, val doOnOk: (T)->Unit, val doOnCancel: ()->Unit) : DialogWrapper(project, false), Cancellable {

    abstract fun getSuccessResult(): T

    override final fun doOKAction() {
        super.doOKAction()
        doOnOk(getSuccessResult())
    }

    override final fun doCancelAction() {
        super.doCancelAction()
        doOnCancel()
    }

    override final fun cancel() {
        close(CANCEL_EXIT_CODE)
    }
}
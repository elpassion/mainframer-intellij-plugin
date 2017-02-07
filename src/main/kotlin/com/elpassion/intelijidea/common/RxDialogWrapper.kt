package com.elpassion.intelijidea.common

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.functions.Cancellable

abstract class RxDialogWrapper<T>(project: Project) : DialogWrapper(project, false), Cancellable {

    private var emitter: Emitter<Result<T>>? = null

    abstract fun getSuccessResult(): T

    override final fun doOKAction() {
        super.doOKAction()
        emitter?.let {
            it.onNext(Result.Success(getSuccessResult()))
            it.onComplete()
            emitter = null
        }
    }

    override final fun doCancelAction() {
        super.doCancelAction()
        emitter?.let {
            it.onNext(Result.Canceled())
            it.onComplete()
            emitter = null
        }
    }

    override final fun cancel() {
        emitter = null
        close(CANCEL_EXIT_CODE)
    }

    override final fun show() {
        throw IllegalAccessError("Use showAsObservable()")
    }

    fun showAsObservable(): Observable<Result<T>> {
        return Observable.create<Result<T>> { emitter ->
            this.emitter = emitter
            emitter.setCancellable(this)
            super.show()
        }
    }
}
package com.elpassion.intelijidea.common

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.reactivex.Observable
import io.reactivex.functions.Cancellable
import io.reactivex.subjects.PublishSubject

abstract class RxDialogWrapper<T>(project: Project) : DialogWrapper(project, false), Cancellable {

    private val subject = PublishSubject.create<Result<T>>()

    abstract fun getSuccessResult(): T

    override final fun doOKAction() {
        super.doOKAction()
        subject.onNext(Result.Success(getSuccessResult()))
        subject.onComplete()
    }

    override final fun doCancelAction() {
        super.doCancelAction()
        subject.onNext(Result.Canceled())
        subject.onComplete()
    }

    override final fun cancel() {
        close(CANCEL_EXIT_CODE)
    }

    override final fun show() {
        throw IllegalAccessError("Use showAsObservable()")
    }

    fun showAsObservable(): Observable<Result<T>> = subject.doOnSubscribe { super.show() }
}
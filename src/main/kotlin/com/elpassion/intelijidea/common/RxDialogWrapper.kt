package com.elpassion.intelijidea.common

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.reactivex.Observable
import io.reactivex.functions.Cancellable
import io.reactivex.subjects.PublishSubject

abstract class RxDialogWrapper<T>(project: Project) : DialogWrapper(project, false), Cancellable {

    private val subject = PublishSubject.create<T>()

    abstract fun getSuccessResult(): T

    override final fun doOKAction() {
        super.doOKAction()
        subject.onNext(getSuccessResult())
        subject.onComplete()
    }

    override final fun doCancelAction() {
        super.doCancelAction()
        subject.onComplete()
    }

    override final fun cancel() {
        close(CANCEL_EXIT_CODE)
    }

    override final fun show() {
        throw IllegalAccessError("Use showAsObservable()")
    }

    fun showAsObservable(): Observable<T> = subject.doOnSubscribe { super.show() }
}
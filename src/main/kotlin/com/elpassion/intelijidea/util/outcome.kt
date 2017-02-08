package com.elpassion.intelijidea.util

import com.elpassion.intelijidea.common.Result
import com.intellij.platform.templates.github.Outcome
import io.reactivex.Observable

fun <V> Outcome<V>.asResultObservable(): Observable<Result<V>> {
    return when {
        isCancelled -> Observable.just(Result.Canceled())
        exception != null -> Observable.error(exception)
        else -> Observable.just(Result.Success(get()!!))
    }
}
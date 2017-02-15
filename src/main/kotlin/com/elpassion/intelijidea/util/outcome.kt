package com.elpassion.intelijidea.util

import com.intellij.platform.templates.github.Outcome
import io.reactivex.Observable

fun <V> Outcome<V>.asResultObservable(): Observable<V> {
    return when {
        isCancelled -> Observable.empty()
        exception != null -> Observable.error(exception)
        else -> Observable.just(get())
    }
}
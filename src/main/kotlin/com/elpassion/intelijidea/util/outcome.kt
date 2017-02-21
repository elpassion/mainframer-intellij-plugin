package com.elpassion.intelijidea.util

import com.intellij.platform.templates.github.Outcome
import io.reactivex.Maybe

fun <V> Outcome<V>.asResultObservable(): Maybe<V> {
    return when {
        isCancelled -> Maybe.empty()
        exception != null -> Maybe.error(exception)
        else -> Maybe.just(get())
    }
}
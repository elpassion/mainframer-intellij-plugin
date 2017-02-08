package com.elpassion.intelijidea.util

import com.elpassion.intelijidea.common.Result
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun <SELF, RESULT> Observable<Result<SELF>>.flatMapResult(function: (SELF) -> Observable<Result<RESULT>>): Observable<Result<RESULT>> {
    return this.flatMap {
        when (it) {
            is Result.Success -> function(it.value)
            is Result.Canceled -> io.reactivex.Observable.just(com.elpassion.intelijidea.common.Result.Canceled())
        }
    }
}

fun <T> Observable<Result<T>>.subscribeResult(
        onSuccess: (Result.Success<T>) -> Unit,
        onCancelled: () -> Unit,
        onError: (Throwable) -> Unit): Disposable {
    return subscribe({
        when (it) {
            is Result.Success -> onSuccess(it)
            is Result.Canceled -> onCancelled()
        }
    }, {
        onError(it)
    })
}
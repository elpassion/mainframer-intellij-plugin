package com.elpassion.intelijidea.util

import com.elpassion.intelijidea.common.Result
import io.reactivex.Observable

fun <SELF, RESULT> Observable<Result<SELF>>.flatMapResult(function: (SELF) -> Observable<Result<RESULT>>): Observable<Result<RESULT>> {
    return this.flatMap {
        when (it) {
            is Result.Success -> function(it.value)
            is Result.Canceled -> io.reactivex.Observable.just(com.elpassion.intelijidea.common.Result.Canceled())
        }
    }
}
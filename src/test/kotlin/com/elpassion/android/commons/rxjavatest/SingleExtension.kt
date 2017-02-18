package com.elpassion.android.commons.rxjavatest

import io.reactivex.Single
import org.mockito.stubbing.OngoingStubbing

fun <T> OngoingStubbing<Single<T>>.thenJust(value: T): OngoingStubbing<Single<T>> = thenReturn(Single.just(value))

fun <T> OngoingStubbing<Single<List<T>>>.thenJust(vararg values: T): OngoingStubbing<Single<List<T>>> = thenReturn(Single.just(values.toList()))

fun <T> OngoingStubbing<Single<T>>.thenError(exception: Exception = RuntimeException()): OngoingStubbing<Single<T>> = thenReturn(Single.error(exception))
package com.elpassion.android.commons.rxjavatest

import io.reactivex.Maybe
import org.mockito.stubbing.OngoingStubbing

fun <T> OngoingStubbing<Maybe<T>>.thenNever(): OngoingStubbing<Maybe<T>> = thenReturn(Maybe.never())

fun <T> OngoingStubbing<Maybe<T>>.thenJust(value: T): OngoingStubbing<Maybe<T>> = thenReturn(Maybe.just(value))

fun <T> OngoingStubbing<Maybe<T>>.thenError(exception: Exception = RuntimeException()): OngoingStubbing<Maybe<T>> = thenReturn(Maybe.error(exception))
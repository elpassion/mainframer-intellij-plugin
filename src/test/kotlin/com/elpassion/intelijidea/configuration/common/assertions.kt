package com.elpassion.intelijidea.configuration.common

fun <T : Exception> assertThrows(exceptionType: Class<T>, function: () -> Unit): T {
    try {
        function.invoke()
    } catch (exception: T) {
        return exception
    }
    throw AssertionError("Expected ${exceptionType.name} not thrown.")
}

package com.elpassion.intelijidea.common

fun <T : Exception> assertThrows(exceptionType: Class<T>, function: () -> Unit): T {
    try {
        function.invoke()
        throw AssertionError("The specified function didn't throw an exception. ")
    } catch (ex: T) {
        when {
            exceptionType.isAssignableFrom(ex.javaClass) -> return ex
            else -> throw AssertionError("Expected ${exceptionType.name} not thrown. Catched ${ex.javaClass.name}")
        }
    }
}
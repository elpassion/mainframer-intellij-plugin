package com.elpassion.intelijidea.common

fun assertThrows(exceptionType: Class<*>, function: () -> Unit): Exception {
    try {
        function.invoke()
        throw AssertionError("The specified function didn't throw an exception. ")
    } catch (ex: Exception) {
        when {
            exceptionType.isAssignableFrom(ex.javaClass) -> return ex
            else -> throw AssertionError("Expected ${exceptionType.name} not thrown. Catched ${ex.javaClass.name}")
        }
    }
}
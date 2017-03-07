package com.elpassion.intelijidea.common

inline fun <reified T : Exception> assertThrows(function: () -> Unit): Exception {
    try {
        function.invoke()
        throw AssertionError("The specified function didn't throw an exception. ")
    } catch (ex: Exception) {
        when {
            T::class.java.isAssignableFrom(ex.javaClass) -> return ex
            else -> throw AssertionError("Expected ${T::class.simpleName} not thrown. Catched ${ex.javaClass.name}")
        }
    }
}
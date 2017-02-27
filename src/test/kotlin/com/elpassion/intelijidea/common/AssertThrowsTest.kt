package com.elpassion.intelijidea.common

import org.junit.Assert.assertTrue
import org.junit.Test


class AssertThrowsTest {

    @Test(expected = AssertionError::class)
    fun shouldInformThatExceptionHasntBeenThrown() {
        assertThrows(Exception::class.java) {
            Unit
        }
    }

    @Test(expected = AssertionError::class)
    fun shouldInformThatTypeOfThrownExceptionIsDifferent() {
        assertThrows(NullPointerException::class.java) {
            throw ClassCastException()
        }
    }

    @Test
    fun shouldReturnExceptionWithCorrectType() {
        val exception = assertThrows(NullPointerException::class.java) {
            throw NullPointerException()
        }
        assertTrue(exception is NullPointerException)
    }

    @Test
    fun shouldAlsoCatchClassesWhichOneInheritsFromBaseClass() {
        assertThrows(NullPointerException::class.java) {
            throw KotlinNullPointerException()
        }
    }

}

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

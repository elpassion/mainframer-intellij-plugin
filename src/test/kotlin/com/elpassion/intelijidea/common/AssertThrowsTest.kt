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
package com.elpassion.mainframerplugin.common

import org.junit.Assert.assertTrue
import org.junit.Test


class AssertThrowsTest {

    @Test(expected = AssertionError::class)
    fun shouldInformThatExceptionHasntBeenThrown() {
        assertThrows<Exception> {
            Unit
        }
    }

    @Test(expected = AssertionError::class)
    fun shouldInformThatTypeOfThrownExceptionIsDifferent() {
        assertThrows<NullPointerException> {
            throw ClassCastException()
        }
    }

    @Test
    fun shouldReturnExceptionWithCorrectType() {
        val exception = assertThrows<NullPointerException> {
            throw NullPointerException()
        }
        assertTrue(exception is NullPointerException)
    }

    @Test
    fun shouldAlsoCatchClassesWhichOneInheritsFromBaseClass() {
        assertThrows<NullPointerException> {
            throw KotlinNullPointerException()
        }
    }

}
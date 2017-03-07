package com.elpassion.intelijidea.util

fun <T> Iterable<T>.indexOfOrNull(element: T): Int? = withIndex().find { it.value == element }?.index
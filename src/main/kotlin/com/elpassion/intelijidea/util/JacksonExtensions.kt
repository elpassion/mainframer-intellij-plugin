package com.elpassion.intelijidea.util

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

fun Any.toJson(): String = ObjectMapper().writeValueAsString(this)

inline fun <reified T : Serializable>  String.fromJson(): T = ObjectMapper().readValue(this, T::class.java)
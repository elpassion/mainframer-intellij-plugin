package com.elpassion.intelijidea.util

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

val objectMapper by lazy { ObjectMapper() }

fun Any.toJson(): String = objectMapper.writeValueAsString(this)

inline fun <reified T : Serializable> String.fromJson(): T? = objectMapper.readValue(this, T::class.java)

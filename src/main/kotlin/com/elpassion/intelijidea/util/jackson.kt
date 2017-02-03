package com.elpassion.intelijidea.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

val objectMapper by lazy { ObjectMapper() }

fun Any.toJson(): String = objectMapper.writeValueAsString(this)

inline fun <reified T : Serializable> String.fromJson(): T? = objectMapper.readValue(this, T::class.java)

inline fun <reified T : Serializable> String.listFromJson(): List<T> = objectMapper.readValue(this, object: TypeReference<List<T>>(){}) ?: emptyList()

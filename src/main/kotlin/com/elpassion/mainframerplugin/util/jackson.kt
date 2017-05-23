package com.elpassion.mainframerplugin.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import java.io.Serializable

val objectMapper by lazy {
    ObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    }
}

fun Any.toJson(): String = objectMapper.writeValueAsString(this)

inline fun <reified T : Serializable> String.fromJson(): T? = objectMapper.readValue(this, T::class.java)

inline fun <reified T> String.listFromJson(): List<T> = objectMapper.readValue(this, object : TypeReference<List<T>>() {}) ?: emptyList()

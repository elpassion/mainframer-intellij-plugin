package com.elpassion.intelijidea.tools

import retrofit2.Call
import retrofit2.Response

fun <T> response(successValue: T): Call<T> {
    return FakeCall<T>(Response.success(successValue), null)
}
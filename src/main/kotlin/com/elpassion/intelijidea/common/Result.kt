package com.elpassion.intelijidea.common

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Canceled<T> : Result<T>()
}
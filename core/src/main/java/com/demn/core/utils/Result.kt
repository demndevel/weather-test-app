package com.demn.core.utils

sealed interface Result<T> {
    data class Success<T>(val value: T) : Result<T>

    class Error<T> : Result<T>
}
package com.anliban.team.hippho.model

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    class Success<T>(val value: T) : Result<T>()
    class Error<T>(val e: Throwable) : Result<T>()
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.value ?: fallback
}
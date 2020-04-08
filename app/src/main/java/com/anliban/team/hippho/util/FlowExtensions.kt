package com.anliban.team.hippho.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import com.anliban.team.hippho.model.Result

fun <T> Flow<T>.toLoadingState(): Flow<Result<T>> = map {
    Result.Success(it) as Result<T>
}
    .onStart {
        emit(Result.Loading)
    }
    .catch {
        emit(Result.Error(it))
    }

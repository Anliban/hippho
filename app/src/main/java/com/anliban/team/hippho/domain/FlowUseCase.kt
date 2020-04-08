package com.anliban.team.hippho.domain

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<T, R> {
    abstract fun execute(parameters: T): Flow<R>
}

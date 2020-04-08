package com.anliban.team.hippho.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class UseCase<in P, R> {

    operator fun invoke(parameters: P, result: MutableLiveData<R>) {
        result.postValue(execute(parameters))
    }

    operator fun invoke(parameters: P): LiveData<R> {
        val liveCallback: MutableLiveData<R> = MutableLiveData()
        this(parameters, liveCallback)
        return liveCallback
    }

    protected abstract fun execute(parameters: P): R
}
package com.anliban.team.hippho.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
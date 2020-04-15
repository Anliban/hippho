package com.anliban.team.hippho.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class InfoViewModel @Inject constructor() : ViewModel() {

    private val _deletedPhotoCount = MutableLiveData<Int>()
    val deletedPhotoCount: LiveData<Int>
        get() = _deletedPhotoCount

    private val _deletedMemoryCount = MutableLiveData<Double>()
    val deletedMemoryCount: LiveData<Double>
        get() = _deletedMemoryCount

    init {
        loadDeletedPhotoCount()
        loadDeletedMemoryCount()
    }

    fun loadDeletedPhotoCount() {
        _deletedPhotoCount.postValue(10)
    }

    fun loadDeletedMemoryCount() {
        _deletedMemoryCount.postValue(12.5)
    }
}

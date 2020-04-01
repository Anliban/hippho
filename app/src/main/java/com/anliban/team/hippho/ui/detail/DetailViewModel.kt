package com.anliban.team.hippho.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.model.Image
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel() {

    private val items = MediatorLiveData<List<Image>>()

    private val _thumbNail = MediatorLiveData<Image>()
    val thumbNail: LiveData<Image>
        get() = _thumbNail

    init {
        _thumbNail.addSource(items) {
            _thumbNail.value = it[0]
        }
    }

    fun setSharedElement(images: List<Image>) {
        items.value = images
    }

}
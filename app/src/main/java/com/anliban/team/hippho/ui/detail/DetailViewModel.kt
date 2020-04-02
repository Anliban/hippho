package com.anliban.team.hippho.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.model.Event
import com.anliban.team.hippho.model.Image
import com.squareup.inject.assisted.AssistedInject

class DetailViewModel @AssistedInject constructor() : ViewModel() {

    private val items = MediatorLiveData<List<Image>>()

    private val _thumbnails = MediatorLiveData<List<Image>>()
    val thumbnails: LiveData<List<Image>>
        get() = _thumbnails

    private val _secondLists = MediatorLiveData<List<Image>>()
    val secondLists: LiveData<List<Image>>
        get() = _secondLists

    private val _moveToThumbnail = MutableLiveData<Event<Int>>()
    val moveToThumbnail: LiveData<Event<Int>>
        get() = _moveToThumbnail

    init {
        _thumbnails.addSource(items) {
            _thumbnails.value = it
        }

        _secondLists.addSource(items) {
            _secondLists.value = it
        }
    }

    fun setSharedElement(images: List<Image>) {
        items.value = images
    }

    fun clickToSecondItem(image: Image) {
        val thumbnails = requireNotNull(_thumbnails.value)
        val position = thumbnails.indexOf(image)

        _moveToThumbnail.value = Event(position)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): DetailViewModel
    }
}
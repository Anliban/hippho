package com.anliban.team.hippho.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.model.Image
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val imageLoader: ImageLoader
) : ViewModel() {

    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>>
        get() = _imageList

    fun loadImages() {
        viewModelScope.launch {
            imageLoader.getImages().collect {
                _imageList.value = it
            }
        }
    }
}
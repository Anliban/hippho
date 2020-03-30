package com.anliban.team.hippho.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.domain.GetImageByDateUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getImageByDateUseCase: GetImageByDateUseCase
) : ViewModel() {

    private val _imageList = MutableLiveData<List<MainUiModel>>()
    val imageList: LiveData<List<MainUiModel>>
        get() = _imageList

    fun loadImages() {
        viewModelScope.launch {
            getImageByDateUseCase.execute(Unit).collect {
                _imageList.value = it
            }
        }
    }
}
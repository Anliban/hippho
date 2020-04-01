package com.anliban.team.hippho.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.domain.GetImageByDateUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getImageByDateUseCase: GetImageByDateUseCase
) : ViewModel() {

    private val _imageList = MutableLiveData<List<HomeUiModel>>()
    val imageList: LiveData<List<HomeUiModel>>
        get() = _imageList

    fun loadImages() {
        viewModelScope.launch {
            getImageByDateUseCase.execute(Unit).collect {
                _imageList.value = it
            }
        }
    }
}
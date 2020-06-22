package com.anliban.team.hippho.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.data.ImageQueryOption
import com.anliban.team.hippho.domain.GetImageByDateUseCase
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.model.Result
import com.anliban.team.hippho.model.successOr
import com.anliban.team.hippho.util.toLoadingState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val getImageByDateUseCase: GetImageByDateUseCase
) : ViewModel() {

    private val homeUiResult = MediatorLiveData<Result<List<HomeUiModel>>>()

    private val _imageList = MediatorLiveData<List<HomeUiModel>>()
    val imageList: LiveData<List<HomeUiModel>>
        get() = _imageList

    val swipeRefreshing: LiveData<Boolean>

    init {

        _imageList.addSource(homeUiResult) { result ->
            _imageList.value = result.successOr(null)
        }

        swipeRefreshing = homeUiResult.map {
            it is Result.Loading
        }
    }

    fun loadImages() {
        viewModelScope.launch {
            getImageByDateUseCase(GetImageRequestParameters(ImageQueryOption.DATE))
                .toLoadingState()
                .collect {
                    homeUiResult.value = it
                }
        }
    }

    fun onSwipeRefresh() {
        loadImages()
    }
}

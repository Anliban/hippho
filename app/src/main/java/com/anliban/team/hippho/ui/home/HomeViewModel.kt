package com.anliban.team.hippho.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.domain.GetImageByDateUseCase
import com.anliban.team.hippho.model.Result
import com.anliban.team.hippho.model.successOr
import com.anliban.team.hippho.util.toLoadingState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getImageByDateUseCase: GetImageByDateUseCase
) : ViewModel() {

    private val homeUiResult = MediatorLiveData<Result<List<HomeUiModel>>>()

    private val _imageList = MediatorLiveData<List<HomeUiModel>>()
    val imageList: LiveData<List<HomeUiModel>>
        get() = _imageList

    private val _loadingProgress = MediatorLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>
        get() = _loadingProgress

    init {

        _imageList.addSource(homeUiResult) { result ->
            _imageList.value = result.successOr(null)
        }

        _loadingProgress.addSource(homeUiResult) {
            _loadingProgress.value = it is Result.Loading
        }

        _loadingProgress.value = true
    }

    fun loadImages() {
        viewModelScope.launch {
            getImageByDateUseCase.execute(Unit)
                .toLoadingState()
                .collect {
                    homeUiResult.value = it
                }
        }
    }
}
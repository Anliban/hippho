package com.anliban.team.hippho.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.data.ImageQueryOption
import com.anliban.team.hippho.domain.GetImageByIdUseCase
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.model.Event
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.model.Result
import com.anliban.team.hippho.model.successOr
import com.anliban.team.hippho.util.toLoadingState
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    private val getImageByDateUseCase: GetImageByIdUseCase,
    @Assisted private val ids: LongArray
) : ViewModel() {

    private val detailResult = MediatorLiveData<Result<List<Image>>>()

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
        _thumbnails.addSource(detailResult) {
            _thumbnails.value = it.successOr(null)
        }

        _secondLists.addSource(detailResult) {
            _secondLists.value = it.successOr(null)
        }

        val request = GetImageRequestParameters(
            option = ImageQueryOption.ID,
            ids = ids.toList()
        )

        viewModelScope.launch {
            getImageByDateUseCase.execute(request)
                .toLoadingState()
                .collect {
                    detailResult.value = it
                }
        }
    }

    fun clickToSecondItem(image: Image) {
        val thumbnails = requireNotNull(_thumbnails.value)
        val position = thumbnails.indexOf(image)

        _moveToThumbnail.value = Event(position)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(ids: LongArray): DetailViewModel
    }
}
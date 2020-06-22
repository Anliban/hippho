package com.anliban.team.hippho.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.data.ImageQueryOption
import com.anliban.team.hippho.domain.DeleteImageUseCase
import com.anliban.team.hippho.domain.GetImageByIdUseCase
import com.anliban.team.hippho.domain.detail.ScaleImageAnimRequestParameters
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImageIndicatorRequestParameters
import com.anliban.team.hippho.domain.detail.SwitchImageIndicatorUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionRequestParameters
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.model.Event
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.model.Result
import com.anliban.team.hippho.model.successOr
import com.anliban.team.hippho.util.requireValue
import com.anliban.team.hippho.util.toLoadingState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    private val getImageByDateUseCase: GetImageByIdUseCase,
    private val switchImagePositionUseCase: SwitchImagePositionUseCase,
    private val switchImageIndicatorUseCase: SwitchImageIndicatorUseCase,
    private val scaleImageAnimUseCase: ScaleImageAnimUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val detailResult = MediatorLiveData<Result<List<Image>>>()

    private val _thumbnails = MediatorLiveData<List<DetailThumbnail>>()
    val thumbnails: LiveData<List<DetailThumbnail>>
        get() = _thumbnails

    private val _secondLists = MediatorLiveData<List<DetailImage>>()
    val secondLists: LiveData<List<DetailImage>>
        get() = _secondLists

    private val _moveToThumbnail = MutableLiveData<Event<Int>>()
    val moveToThumbnail: LiveData<Event<Int>>
        get() = _moveToThumbnail

    private val clickedId = MediatorLiveData<Long>()

    val organizeImageState = MediatorLiveData<Boolean>()
    val organizeImagesState = MediatorLiveData<Boolean>()

    private val _navigateToHome = MutableLiveData<Event<Unit>>()
    val navigateToHome: LiveData<Event<Unit>>
        get() = _navigateToHome

    private val _showEmptySelected = MutableLiveData<Event<Unit>>()
    val showEmptySelected: LiveData<Event<Unit>>
        get() = _showEmptySelected

    private val _requestDeleteImage = MutableLiveData<Event<Unit>>()
    val requestDeleteImage: LiveData<Event<Unit>>
        get() = _requestDeleteImage

    init {
        _thumbnails.addSource(detailResult) {
            _thumbnails.value = it.successOr(null)?.mapDetailUiModel()
        }

        _secondLists.addSource(detailResult) {
            val images = it.successOr(null)?.mapDetailImageList()
            clickedId.value = images?.get(0)?.image?.id
            _secondLists.value = images
        }

        organizeImageState.addSource(clickedId) { id ->
            _secondLists.value?.let { images ->
                organizeImageState.value = images.find { it.image.id == id }?.isScaled
            }
        }

        organizeImageState.addSource(secondLists) { images ->
            clickedId.value?.let { id ->
                organizeImageState.value = images.find { it.image.id == id }?.isScaled
            }
        }

        organizeImagesState.addSource(secondLists) { images ->
            organizeImagesState.value = images?.all { it.isScaled }
        }
        val ids = savedStateHandle.requireValue<LongArray>("ids")
        val request = GetImageRequestParameters(
            option = ImageQueryOption.ID,
            ids = ids.toList()
        )

        viewModelScope.launch {
            getImageByDateUseCase(request)
                .toLoadingState()
                .collect {
                    detailResult.value = it
                }
        }
    }

    fun clickToSecondItem(item: DetailUiModel) {

        require(item is DetailImage)
        val request = SwitchImagePositionRequestParameters(
            model = item,
            items = _secondLists.value,
            clickedId = clickedId
        )
        switchImagePositionUseCase(request, _secondLists)

        val thumbnails = requireNotNull(_thumbnails.value)
        val position = thumbnails.indexOf(thumbnails.find { it.image == item.image })
        _moveToThumbnail.value = Event(position)
    }

    fun changeIndicatorOfSecondList(position: Int) {
        val request = SwitchImageIndicatorRequestParameters(
            position,
            items = _secondLists.value,
            clickedId = clickedId
        )
        switchImageIndicatorUseCase(request, _secondLists)
    }

    fun selectImage() {
        val request = ScaleImageAnimRequestParameters(
            clickedId = clickedId,
            items = _secondLists.value,
            type = OrganizeImage.Single
        )

        scaleImageAnimUseCase(request, _secondLists)
    }

    fun selectImages() {
        val request = ScaleImageAnimRequestParameters(
            clickedId = clickedId,
            items = _secondLists.value,
            type = OrganizeImage.All,
            allState = organizeImagesState.value
        )

        scaleImageAnimUseCase(request, _secondLists)
    }

    fun requestDeleteImages() {
        val isEmpty = getSelectedImages().isNullOrEmpty()

        if (isEmpty) {
            _showEmptySelected.value = Event(Unit)
        } else {
            _requestDeleteImage.value = Event(Unit)
        }
    }

    fun deleteImages() {
        getSelectedImages()?.let {
            viewModelScope.launch {
                deleteImageUseCase.execute(it)
                _navigateToHome.value = Event(Unit)
            }
        }
    }

    private fun getSelectedImages(): List<Image>? {
        return _secondLists.value
            ?.filter { it.isScaled }
            ?.map { it.image }
    }
}

package com.anliban.team.hippho.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.data.ImageQueryOption
import com.anliban.team.hippho.domain.DeleteImageUseCase
import com.anliban.team.hippho.domain.GetImageByIdUseCase
import com.anliban.team.hippho.domain.detail.ScaleImageAnimRequestParameters
import com.anliban.team.hippho.domain.detail.ScaleImageAnimUseCase
import com.anliban.team.hippho.domain.detail.SwitchImagePositionRequestParameters
import com.anliban.team.hippho.domain.detail.SwitchImagePositionUseCase
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
    private val switchImagePositionUseCase: SwitchImagePositionUseCase,
    private val scaleImageAnimUseCase: ScaleImageAnimUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    @Assisted private val ids: LongArray
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

    fun organizeImage() {
        val images = _secondLists.value
            ?.filter { it.isScaled }
            ?.map { it.image }

        images?.let {
            viewModelScope.launch {
                deleteImageUseCase.execute(it)
                _navigateToHome.value = Event(Unit)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(ids: LongArray): DetailViewModel
    }
}

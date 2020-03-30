package com.anliban.team.hippho.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.model.Image
import com.anliban.team.hippho.ui.home.HomeUiModel
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel() {

    private val items = MediatorLiveData<HomeUiModel>()

    private val _thumbNail = MediatorLiveData<Image>()
    val thumbNail: LiveData<Image>
        get() = _thumbNail

    init {
        _thumbNail.addSource(items) {
            _thumbNail.value = it.item[0]
        }
    }

    fun setSharedElement(uiModel: HomeUiModel) {
        items.value = uiModel
    }

}
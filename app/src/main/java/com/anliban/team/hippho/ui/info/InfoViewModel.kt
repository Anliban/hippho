package com.anliban.team.hippho.ui.info

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anliban.team.hippho.domain.info.LoadInfoDataResult
import com.anliban.team.hippho.domain.info.LoadInfoDataUseCase
import com.anliban.team.hippho.model.successOr
import com.anliban.team.hippho.util.bytesToMegaBytes
import com.anliban.team.hippho.util.toDecimal
import kotlinx.coroutines.launch

class InfoViewModel @ViewModelInject constructor(
    loadInfoDataUseCase: LoadInfoDataUseCase
) : ViewModel() {

    private val loadInfoResult = MediatorLiveData<LoadInfoDataResult>()

    val deletedPhotoCount: LiveData<String> = Transformations.map(loadInfoResult) {
        it.deletedImageCount.toDouble().toDecimal()
    }

    val deletedMemoryCount: LiveData<String> = Transformations.map(loadInfoResult) {
        it.deletedFileSize.bytesToMegaBytes().toDecimal()
    }

    init {
        viewModelScope.launch {
            loadInfoResult.value = loadInfoDataUseCase(Unit).successOr(null)
        }
    }
}

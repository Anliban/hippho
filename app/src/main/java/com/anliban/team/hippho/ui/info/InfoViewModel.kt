package com.anliban.team.hippho.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.anliban.team.hippho.domain.info.LoadInfoDataResult
import com.anliban.team.hippho.domain.info.LoadInfoDataUseCase
import com.anliban.team.hippho.util.bytesToMegaBytes
import com.anliban.team.hippho.util.toDecimal
import javax.inject.Inject

class InfoViewModel @Inject constructor(
    loadInfoDataUseCase: LoadInfoDataUseCase
) : ViewModel() {

    private val loadInfoResult = MediatorLiveData<LoadInfoDataResult>()

    val deletedPhotoCount: LiveData<String> = Transformations.map(loadInfoResult) {
        it.deletedImageCount.toDouble().toDecimal()
    }

    val deletedMemoryCount: LiveData<String> = Transformations.map(loadInfoResult) {
        println(it)
        it.deletedFileSize.bytesToMegaBytes().toDecimal()
    }

    init {
        loadInfoDataUseCase(Unit, loadInfoResult)
    }
}

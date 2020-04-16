package com.anliban.team.hippho.domain.info

import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.domain.UseCase

class LoadInfoDataUseCase(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, LoadInfoDataResult>() {
    override fun execute(parameters: Unit): LoadInfoDataResult {
        return LoadInfoDataResult(
            deletedImageCount = preferenceStorage.deletedCount,
            deletedFileSize = preferenceStorage.deletedFileSize
        )
    }
}

data class LoadInfoDataResult(
    val deletedImageCount: Long,
    val deletedFileSize: Long
)

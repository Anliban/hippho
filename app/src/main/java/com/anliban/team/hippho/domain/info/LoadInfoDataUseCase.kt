package com.anliban.team.hippho.domain.info

import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.di.qualifier.IoDispatcher
import com.anliban.team.hippho.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class LoadInfoDataUseCase(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Unit, LoadInfoDataResult>(ioDispatcher) {

    override suspend fun execute(parameters: Unit): LoadInfoDataResult {
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

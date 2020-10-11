package com.anliban.team.hippho.domain.image

import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.data.pref.PreferenceStorage
import com.anliban.team.hippho.model.Image

class DeleteImageUseCase(
    private val mediaProvider: MediaProvider,
    private val preferenceStorage: PreferenceStorage
) {

    suspend fun execute(parameters: List<Image>) {
        try {
            mediaProvider.delete(parameters)
            updateDeletedStorage(parameters)
        } catch (e: Exception) {
        }
    }

    private fun updateDeletedStorage(images: List<Image>) {
        images.forEach {
            preferenceStorage.deletedCount += 1
            preferenceStorage.deletedFileSize += it.fileSize
        }
    }
}

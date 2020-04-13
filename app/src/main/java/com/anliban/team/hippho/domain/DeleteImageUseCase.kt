package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.model.Image

class DeleteImageUseCase(private val mediaProvider: MediaProvider) {

    suspend fun execute(parameters: List<Image>) {
        mediaProvider.delete(parameters)
    }
}

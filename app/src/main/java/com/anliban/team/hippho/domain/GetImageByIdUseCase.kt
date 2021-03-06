package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.model.Image
import kotlinx.coroutines.flow.Flow

class GetImageByIdUseCase(
    private val mediaProvider: MediaProvider
) : FlowUseCase<GetImageRequestParameters, List<Image>>() {

    override fun execute(parameters: GetImageRequestParameters): Flow<List<Image>> {
        return mediaProvider.getImages(parameters.option, parameters.ids)
    }
}

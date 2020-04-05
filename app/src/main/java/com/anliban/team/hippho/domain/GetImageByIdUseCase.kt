package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.model.Image
import kotlinx.coroutines.flow.Flow

class GetImageByIdUseCase(
    private val imageLoader: ImageLoader
) : FlowUseCase<GetImageRequestParameters, List<Image>>() {

    override fun execute(parameters: GetImageRequestParameters): Flow<List<Image>> {
        return imageLoader.getImages(parameters.option, parameters.ids)
    }
}

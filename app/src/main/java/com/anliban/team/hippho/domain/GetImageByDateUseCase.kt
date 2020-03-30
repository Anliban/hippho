package com.anliban.team.hippho.domain

import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.ui.MainUiModel
import com.anliban.team.hippho.util.midNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByDateUseCase(
    private val imageLoader: ImageLoader
) : FlowUseCase<Unit, List<MainUiModel>>() {

    override fun execute(parameters: Unit): Flow<List<MainUiModel>> {
        return imageLoader.getImages().map { images ->
            images.groupBy {
                it.date.midNight()
            }.map {
                MainUiModel(it.key, it.value)
            }
        }
    }
}
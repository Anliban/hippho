package com.anliban.team.hippho.domain

import com.anliban.team.hippho.core.ImageSimilarFinder
import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.util.midNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByDateUseCase(
    private val imageLoader: ImageLoader,
    private val imageSimilarFinder: ImageSimilarFinder
) : FlowUseCase<Unit, List<HomeUiModel>>() {

    override fun execute(parameters: Unit): Flow<List<HomeUiModel>> {
        return imageLoader.getImages()
            .map { images ->
                images.groupBy {
                    it.date.midNight()
                }.map {
                    HomeUiModel(it.key, it.value)
                }
            }
    }
}
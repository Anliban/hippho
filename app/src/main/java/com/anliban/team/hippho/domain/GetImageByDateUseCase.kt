package com.anliban.team.hippho.domain

import com.anliban.team.hippho.core.ImageSimilarFinder
import com.anliban.team.hippho.data.ImageLoader
import com.anliban.team.hippho.data.ImageQueryOption
import com.anliban.team.hippho.ui.home.HomeListContent
import com.anliban.team.hippho.ui.home.HomeListHeader
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.util.midNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByDateUseCase(
    private val imageLoader: ImageLoader,
    private val imageSimilarFinder: ImageSimilarFinder
) : FlowUseCase<ImageQueryOption, List<HomeUiModel>>() {

    override fun execute(parameters: ImageQueryOption): Flow<List<HomeUiModel>> {
        return imageLoader.getImages(parameters)
            .map { images ->
                val groupByDate = images.groupBy { it.date.midNight() }
                groupByDate
                    .flatMap {
                        imageSimilarFinder.calculate(it.key, it.value)
                    }.groupBy {
                        it.first
                    }.flatMap {
                        val header = HomeListHeader(it.key)
                        val content = it.value.map { v -> HomeListContent(v.second) }
                        listOf(header) + content
                    }
            }
    }
}

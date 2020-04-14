package com.anliban.team.hippho.domain
import com.anliban.team.hippho.data.ImageLoadHelper
import com.anliban.team.hippho.data.MediaProvider
import com.anliban.team.hippho.domain.model.GetImageRequestParameters
import com.anliban.team.hippho.ui.home.HomeListContent
import com.anliban.team.hippho.ui.home.HomeListHeader
import com.anliban.team.hippho.ui.home.HomeUiModel
import com.anliban.team.hippho.util.midNight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetImageByDateUseCase(
    private val mediaProvider: MediaProvider,
    private val imageLoadHelper: ImageLoadHelper
) : FlowUseCase<GetImageRequestParameters, List<HomeUiModel>>() {

    override fun execute(parameters: GetImageRequestParameters): Flow<List<HomeUiModel>> {
        return mediaProvider.getImages(parameters.option)
            .map { images ->
                images.groupBy { it.date.midNight() }
                    .flatMap { imageLoadHelper.groupByTimeInterval(it.key, it.value) }
                    .groupBy { it.first }
                    .flatMap {
                        val header = HomeListHeader(it.key)
                        val content = (it.value.map { it2 -> HomeListContent(it2.second) })
                        listOf(header) + content
                    }
            }
    }
}
